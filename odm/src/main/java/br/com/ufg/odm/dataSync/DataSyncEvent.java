package br.com.ufg.odm.dataSync;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataSyncEvent {
    private String eventId;
    private String entityType;
    private String operation;
    private Long entityId;

    private Object dataJson;

    private LocalDateTime timestamp;
}
