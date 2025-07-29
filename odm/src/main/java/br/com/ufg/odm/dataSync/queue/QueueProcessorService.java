package br.com.ufg.odm.dataSync.queue;

import br.com.ufg.odm.dataSync.DataSyncHandler;
import br.com.ufg.odm.dataSync.EntityType;
import br.com.ufg.odm.dataSync.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueProcessorService {

    private final OperationQueueService queueService;
    private final DataSyncHandler dataSyncHandler;

    @Scheduled(fixedDelay = 1000) // Processa a cada 1 segundo
    @Async
    public void processQueue() {
        try {
            if (queueService.getFailedSize() > 0) {
                return;
            }
            QueuedOperation operation = queueService.dequeueOperation();

            if (operation != null) {
                log.info("Processando operação da fila: {} - {} - ID: {} (tentativa {})",
                    operation.getEntityType(), operation.getOperation(),
                    operation.getEntityId(), operation.getAttempts());

                try {
                    EntityType entityType = EntityType.fromValue(operation.getEntityType());
                    OperationType operationType = OperationType.valueOf(operation.getOperation());

                    dataSyncHandler.handleSyncEvent(entityType, operationType,
                        operation.getEntityId(), operation.getDataJson());

                    queueService.markOperationAsCompleted(operation);

                } catch (Exception e) {
                    String errorMessage = String.format("Erro ao processar operação: %s", e.getMessage());
                    log.error(errorMessage, e);
                    queueService.markOperationAsFailed(operation, errorMessage);
                }
            }

        } catch (Exception e) {
            log.error("Erro no processador da fila", e);
        }
    }

    @Scheduled(fixedDelay = 60000) // Log de status a cada minuto
    public void logQueueStatus() {
        long queueSize = queueService.getQueueSize();
        long processingSize = queueService.getProcessingSize();
        long failedSize = queueService.getFailedSize();

        if (queueSize > 0 || processingSize > 0 || failedSize > 0) {
            log.info("Status da fila - Pendentes: {}, Processando: {}, Falhadas: {}",
                queueSize, processingSize, failedSize);
        }
    }
}
