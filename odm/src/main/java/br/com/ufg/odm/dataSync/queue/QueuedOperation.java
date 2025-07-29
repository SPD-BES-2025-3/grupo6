package br.com.ufg.odm.dataSync.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueuedOperation {

    private String operationId;
    private String entityType;
    private String operation;
    private Long entityId;
    private String dataJson;

    private LocalDateTime enqueuedAt;
    private LocalDateTime processingStartedAt;
    private LocalDateTime lastAttemptAt;

    private int attempts;
    private int maxRetries = 3;
    private String lastError;

    public QueuedOperation(String entityType, String operation, Long entityId, String dataJson) {
        this.operationId = generateOperationId(entityType, operation, entityId);
        this.entityType = entityType;
        this.operation = operation;
        this.entityId = entityId;
        this.dataJson = dataJson;
        this.attempts = 0;
        this.maxRetries = 3;
    }

    private String generateOperationId(String entityType, String operation, Long entityId) {
        return String.format("%s_%s_%d_%d", entityType, operation, entityId, System.currentTimeMillis());
    }
}

