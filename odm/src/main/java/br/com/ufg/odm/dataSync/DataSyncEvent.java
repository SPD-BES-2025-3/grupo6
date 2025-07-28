package br.com.ufg.odm.dataSync;

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
}
