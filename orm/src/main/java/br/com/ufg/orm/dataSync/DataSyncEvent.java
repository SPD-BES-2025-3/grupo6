package br.com.ufg.orm.dataSync;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DataSyncEvent {
    private String eventId;
    private String entityType;
    private String operation;
    private Long entityId;

    @JsonRawValue
    private String dataJson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public DataSyncEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public DataSyncEvent(EntityType entityType, OperationType operation, Long entityId, String dataJson) {
        this();
        this.entityType = entityType.getValue();
        this.operation = operation.name();
        this.entityId = entityId;
        this.dataJson = dataJson;
        this.eventId = generateEventId();
    }

    private String generateEventId() {
        return entityType + "_" + operation + "_" + System.currentTimeMillis();
    }
}
