package br.com.ufg.odm.dataSync.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationQueueService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String QUEUE_KEY = "odm:operation_queue";
    private static final String PROCESSING_KEY = "odm:processing_operations";
    private static final String FAILED_KEY = "odm:failed_operations";

    public void enqueueOperation(QueuedOperation operation) {
        try {
            operation.setEnqueuedAt(LocalDateTime.now());
            operation.setAttempts(0);

            String operationJson = objectMapper.writeValueAsString(operation);
            redisTemplate.opsForList().leftPush(QUEUE_KEY, operationJson);

            log.info("Operação enfileirada: {} - {} - ID: {}",
                operation.getEntityType(), operation.getOperation(), operation.getEntityId());

        } catch (Exception e) {
            log.error("Erro ao enfileirar operação", e);
        }
    }

    public QueuedOperation dequeueOperation() {
        try {
            String operationJson = redisTemplate.opsForList().rightPopAndLeftPush(QUEUE_KEY, PROCESSING_KEY);

            if (operationJson != null) {
                QueuedOperation operation = objectMapper.readValue(operationJson, QueuedOperation.class);
                operation.setProcessingStartedAt(LocalDateTime.now());
                operation.setAttempts(operation.getAttempts() + 1);

                // Atualiza a operação no conjunto de processamento
                updateProcessingOperation(operation);

                return operation;
            }

        } catch (Exception e) {
            log.error("Erro ao desenfileirar operação", e);
        }

        return null;
    }

    public void markOperationAsCompleted(QueuedOperation operation) {
        try {
            String operationJson = objectMapper.writeValueAsString(operation);
            redisTemplate.opsForList().remove(PROCESSING_KEY, 1, operationJson);

            log.info("Operação concluída com sucesso: {} - {} - ID: {}",
                operation.getEntityType(), operation.getOperation(), operation.getEntityId());

        } catch (Exception e) {
            log.error("Erro ao marcar operação como concluída", e);
        }
    }

    public void markOperationAsFailed(QueuedOperation operation, String errorMessage) {
        try {
            operation.setLastError(errorMessage);
            operation.setLastAttemptAt(LocalDateTime.now());

            String operationJson = objectMapper.writeValueAsString(operation);

            // Remove da lista de processamento
            redisTemplate.opsForList().remove(PROCESSING_KEY, 1, operationJson);

            // Se ainda tem tentativas, recoloca na fila
            if (operation.getAttempts() < operation.getMaxRetries()) {
                redisTemplate.opsForList().leftPush(QUEUE_KEY, operationJson);
                log.warn("Operação falhou (tentativa {}/{}), recolocada na fila: {} - {} - ID: {}",
                    operation.getAttempts(), operation.getMaxRetries(),
                    operation.getEntityType(), operation.getOperation(), operation.getEntityId());
            } else {
                // Esgotou as tentativas, move para fila de falhas
                redisTemplate.opsForList().leftPush(FAILED_KEY, operationJson);
                log.error("Operação falhou definitivamente após {} tentativas: {} - {} - ID: {}",
                    operation.getMaxRetries(),
                    operation.getEntityType(), operation.getOperation(), operation.getEntityId());
            }

        } catch (Exception e) {
            log.error("Erro ao marcar operação como falhada", e);
        }
    }

    public long getQueueSize() {
        return redisTemplate.opsForList().size(QUEUE_KEY);
    }

    public long getProcessingSize() {
        return redisTemplate.opsForList().size(PROCESSING_KEY);
    }

    public long getFailedSize() {
        return redisTemplate.opsForList().size(FAILED_KEY);
    }

    public boolean hasOperationsInQueue() {
        long queueSize = getQueueSize();
        long processingSize = getProcessingSize();
        return queueSize > 0 || processingSize > 0;
    }

    public List<String> getFailedOperations() {
        return redisTemplate.opsForList().range(FAILED_KEY, 0, -1);
    }

    public void requeueFailedOperations() {
        try {
            List<String> failedOperations = redisTemplate.opsForList().range(FAILED_KEY, 0, -1);

            for (String operationJson : failedOperations) {
                QueuedOperation operation = objectMapper.readValue(operationJson, QueuedOperation.class);
                operation.setAttempts(0); // Reset tentativas
                operation.setLastError(null);

                String resetOperationJson = objectMapper.writeValueAsString(operation);
                redisTemplate.opsForList().leftPush(QUEUE_KEY, resetOperationJson);
            }

            // Limpa a fila de falhas
            redisTemplate.delete(FAILED_KEY);

            log.info("Recolocadas {} operações falhadas na fila", failedOperations.size());

        } catch (Exception e) {
            log.error("Erro ao recolocar operações falhadas", e);
        }
    }

    private void updateProcessingOperation(QueuedOperation operation) {
        try {
            String operationJson = objectMapper.writeValueAsString(operation);
            // Remove a versão antiga e adiciona a nova
            redisTemplate.opsForList().remove(PROCESSING_KEY, 1, operationJson);
            redisTemplate.opsForList().leftPush(PROCESSING_KEY, operationJson);
        } catch (Exception e) {
            log.error("Erro ao atualizar operação em processamento", e);
        }
    }
}
