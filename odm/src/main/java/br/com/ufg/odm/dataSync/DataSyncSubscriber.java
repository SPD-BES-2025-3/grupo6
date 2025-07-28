package br.com.ufg.odm.dataSync;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            log.info("Evento de sincronização recebido: {}", messageBody);

            DataSyncEvent event = objectMapper.readValue(messageBody, DataSyncEvent.class);

            EntityType entityType = EntityType.fromValue(event.getEntityType());
            OperationType operation = OperationType.valueOf(event.getOperation());

            dataSyncHandler.handleSyncEvent(entityType, operation, event.getEntityId(), event.getDataJson());

        } catch (Exception e) {
            log.error("Erro ao processar evento de sincronização: {}", new String(message.getBody()), e);
        }
    }
}
