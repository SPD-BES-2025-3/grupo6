package br.com.ufg.orm.dataSync;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSyncPublisher {

    private static final String SYNC_CHANNEL = "data-sync-channel";

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    public void publishEvent(EntityType entityType, OperationType operation, Long entityId, Object data) {
        try {
            String dataJson = null;
            if (data != null) {
                dataJson = objectMapper.writeValueAsString(data);
            }

            DataSyncEvent event = new DataSyncEvent(entityType, operation, entityId, dataJson);

            String eventJson = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(SYNC_CHANNEL, eventJson);

            log.info("Evento publicado: {} - {} - ID: {}", entityType, operation, entityId);

        } catch (Exception e) {
            log.error("Erro ao publicar evento de sincronização", e);
            throw new RuntimeException("Falha na publicação do evento", e);
        }
    }

    public void publishCreateEvent(EntityType entityType, Long entityId, Object data) {
        publishEvent(entityType, OperationType.CREATE, entityId, data);
    }

    public void publishUpdateEvent(EntityType entityType, Long entityId, Object data) {
        publishEvent(entityType, OperationType.UPDATE, entityId, data);
    }

    public void publishDeleteEvent(EntityType entityType, Long entityId) {
        publishEvent(entityType, OperationType.DELETE, entityId, null);
    }
}
