package br.com.ufg.odm.dataSync;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSyncHandler {

    private final ObjectMapper objectMapper;
    private final EntitySyncService entitySyncService;

    public void handleSyncEvent(EntityType entityType, OperationType operation, Long entityId, String dataJson) {
        try {
            log.info("Processando evento: {} - {} - ID: {}", entityType, operation, entityId);

            switch (operation) {
                case CREATE:
                    handleCreateEvent(entityType, entityId, dataJson);
                    break;
                case UPDATE:
                    handleUpdateEvent(entityType, entityId, dataJson);
                    break;
                case DELETE:
                    handleDeleteEvent(entityType, entityId);
                    break;
                default:
                    log.warn("Operação não reconhecida: {}", operation);
            }

        } catch (Exception e) {
            log.error("Erro ao processar evento de sincronização para {} - {}", entityType, operation, e);
        }
    }

    private void handleCreateEvent(EntityType entityType, Long entityId, String dataJson) {
        if (dataJson != null) {
            entitySyncService.createEntity(entityType, entityId, dataJson);
        }
    }

    private void handleUpdateEvent(EntityType entityType, Long entityId, String dataJson) {
        if (dataJson != null) {
            entitySyncService.updateEntity(entityType, entityId, dataJson);
        }
    }

    private void handleDeleteEvent(EntityType entityType, Long entityId) {
        entitySyncService.deleteEntity(entityType, entityId);
    }
}
