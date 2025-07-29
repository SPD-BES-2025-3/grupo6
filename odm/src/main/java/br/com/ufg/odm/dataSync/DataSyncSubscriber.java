package br.com.ufg.odm.dataSync;

import br.com.ufg.odm.dataSync.queue.OperationQueueService;
import br.com.ufg.odm.dataSync.queue.QueuedOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSyncSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final DataSyncHandler dataSyncHandler;
    private final Gson gson;
    private final OperationQueueService queueService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = objectMapper.readValue(message.getBody(), String.class);
            log.info("Evento de sincronização recebido: {}", messageBody);

            DataSyncEvent event = gson.fromJson(messageBody, DataSyncEvent.class);

            EntityType entityType = EntityType.fromValue(event.getEntityType());
            OperationType operation = OperationType.valueOf(event.getOperation());

            // Verifica se há operações pendentes na fila para manter a ordem
            if (queueService.hasOperationsInQueue()) {
                log.info("Existem operações pendentes na fila. Adicionando nova operação para manter a ordem: {} - {} - ID: {}",
                    entityType, operation, event.getEntityId());

                QueuedOperation queuedOperation = new QueuedOperation(
                    event.getEntityType(),
                    event.getOperation(),
                    event.getEntityId(),
                    objectMapper.writeValueAsString(event.getDataJson())
                );

                queueService.enqueueOperation(queuedOperation);
            }else {
                // Se não há operações na fila, tenta processar diretamente
                try {
                    dataSyncHandler.handleSyncEvent(entityType, operation, event.getEntityId(),
                            objectMapper.writeValueAsString(event.getDataJson()));

                    log.info("Operação de sincronização processada com sucesso: {} - {} - ID: {}",
                            entityType, operation, event.getEntityId());

                } catch (Exception e) {
                    log.warn("Falha ao processar operação diretamente, adicionando à fila persistente: {} - {} - ID: {}",
                            entityType, operation, event.getEntityId(), e);

                    // Se falhar, adiciona à fila persistente
                    QueuedOperation queuedOperation = new QueuedOperation(
                            event.getEntityType(),
                            event.getOperation(),
                            event.getEntityId(),
                            objectMapper.writeValueAsString(event.getDataJson())
                    );

                    queueService.enqueueOperation(queuedOperation);
                }
            }
        } catch (Exception e) {
            log.error("Erro ao processar evento de sincronização: {}", new String(message.getBody()), e);
        }
    }
}
