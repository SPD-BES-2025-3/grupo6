package br.com.ufg.odm.dataSync;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntitySyncService {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    private static final Map<EntityType, String> COLLECTION_NAMES = Map.of(
        EntityType.USUARIO, "usuarios",
        EntityType.LIVRO, "livros",
        EntityType.EXEMPLAR, "exemplares",
        EntityType.RESERVA, "reservas",
        EntityType.EMPRESTIMO, "emprestimos"
    );

    public void createEntity(EntityType entityType, Long entityId, String dataJson) {
        try {
            String collectionName = getCollectionName(entityType);
            Map<String, Object> document = parseJsonToDocument(dataJson);
            document.put("_id", entityId);

            mongoTemplate.save(document, collectionName);
            log.info("Entidade criada no MongoDB: {} - ID: {}", entityType, entityId);

        } catch (Exception e) {
            log.error("Erro ao criar entidade {} - ID: {}", entityType, entityId, e);
        }
    }

    public void updateEntity(EntityType entityType, Long entityId, String dataJson) {
        try {
            String collectionName = getCollectionName(entityType);
            Map<String, Object> document = parseJsonToDocument(dataJson);
            document.put("_id", entityId);

            Query query = new Query(Criteria.where("_id").is(entityId));

            if (mongoTemplate.exists(query, collectionName)) {
                mongoTemplate.save(document, collectionName);
                log.info("Entidade atualizada no MongoDB: {} - ID: {}", entityType, entityId);
            } else {
                // Se não existe, cria
                mongoTemplate.save(document, collectionName);
                log.info("Entidade criada no MongoDB (não existia): {} - ID: {}", entityType, entityId);
            }

        } catch (Exception e) {
            log.error("Erro ao atualizar entidade {} - ID: {}", entityType, entityId, e);
        }
    }

    public void deleteEntity(EntityType entityType, Long entityId) {
        try {
            String collectionName = getCollectionName(entityType);
            Query query = new Query(Criteria.where("_id").is(entityId));

            mongoTemplate.remove(query, collectionName);
            log.info("Entidade removida do MongoDB: {} - ID: {}", entityType, entityId);

        } catch (Exception e) {
            log.error("Erro ao remover entidade {} - ID: {}", entityType, entityId, e);
        }
    }

    private String getCollectionName(EntityType entityType) {
        return COLLECTION_NAMES.get(entityType);
    }

    private Map<String, Object> parseJsonToDocument(String dataJson) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(dataJson);
        Map<String, Object> document = new HashMap<>();

        jsonNode.fieldNames().forEachRemaining(fieldName -> {
            JsonNode value = jsonNode.get(fieldName);

            if (value.isNull()) {
                document.put(fieldName, null);
            } else if (value.isBoolean()) {
                document.put(fieldName, value.asBoolean());
            } else if (value.isInt()) {
                document.put(fieldName, value.asInt());
            } else if (value.isLong()) {
                document.put(fieldName, value.asLong());
            } else if (value.isDouble()) {
                document.put(fieldName, value.asDouble());
            } else {
                document.put(fieldName, value.asText());
            }
        });

        return document;
    }
}
