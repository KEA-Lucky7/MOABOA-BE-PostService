package com.example.lucky7postservice.utils.debezium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KafkaMessageListener {
    private final DataSource queryDatasource;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaMessageListener(@Qualifier("queryDatasource") DataSource queryDatasource) {
        this.queryDatasource = queryDatasource;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "test-debezium-connector", groupId = "test-debezium", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        log.info("Received message: Key: {}, Value: {}", record.key(), record.value());
        try {
            JsonNode jsonNode = objectMapper.readTree(record.value());
            String databaseName = jsonNode.get("source").get("db") != null ? jsonNode.get("source").get("db").asText() : null;
            String tableName = jsonNode.get("source").get("table") != null ? jsonNode.get("source").get("table").asText() : null;
            String operation = jsonNode.get("op") != null ? jsonNode.get("op").asText() : null;

            processMessage(jsonNode, queryDatasource, databaseName, tableName, operation);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process message", e);
        }
    }

    private Schema createSchema(JsonNode jsonNode) {
        SchemaBuilder builder = SchemaBuilder.struct();

        jsonNode.fieldNames().forEachRemaining(fieldName -> {
            JsonNode fieldNode = jsonNode.get(fieldName);
            Schema fieldSchema = inferSchema(fieldNode);
            builder.field(fieldName, fieldSchema);
        });

        return builder.build();
    }

    private Schema inferSchema(JsonNode fieldNode) {
        if (fieldNode.isTextual()) {
            return Schema.STRING_SCHEMA;
        } else if (fieldNode.isInt() || fieldNode.isLong()) {
            return Schema.INT64_SCHEMA;
        } else if (fieldNode.isBoolean()) {
            return Schema.BOOLEAN_SCHEMA;
        } else if (fieldNode.isDouble()) {
            return Schema.FLOAT64_SCHEMA;
        } else {
            return Schema.STRING_SCHEMA;
        }
    }

    private Object getValueFromJsonNode(JsonNode node, Schema schema) {
        return switch (schema.type()) {
            case INT64 -> node.asLong();
            case BOOLEAN -> node.asBoolean();
            case FLOAT64 -> node.asDouble();
            default -> node.asText();
        };
    }

    private void processMessage(JsonNode sourceRecordValue, DataSource dataSource, String databaseName, String tableName, String operation) throws SQLException {
        if (sourceRecordValue == null) {
            log.warn("Received null value for source record");
            return;
        }

        log.debug("Table name: {}", tableName);
        log.debug("Database name: {}", databaseName);
        log.debug("Operation = {}", operation);

        if (tableName == null || databaseName == null || operation == null) {
            log.info("Operation 메시지가 아닙니다.");
            return;
        }

        if(operation.equals("r")) {
            log.info("Read operation detected, no database operation will be performed.");
            return;
        }

        databaseName="moaboa_query";
        String sql;
        switch (operation) {
            case "c" -> {
                Struct after = convertToStruct(sourceRecordValue.get("after"));

                long id = after.getInt64("id");
                log.debug("Object ID : {}", id);

                sql = generateCreateSQL(databaseName, tableName, after);
                log.debug(sql);
            }
            case "u" -> {
                Struct after = convertToStruct(sourceRecordValue.get("after"));

                long id = after.getInt64("id");
                log.debug("Object ID : {}", id);

                sql = generateUpdateSQL(databaseName, tableName, after) + id;
                log.debug(sql);
            }
            case "d" -> {
                sql = generateDeleteSQL(databaseName, tableName);
                log.debug(sql);
            }
            default -> {
                log.warn("Unsupported operation: {}", operation);
                return;
            }
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int index = 1;
                Struct after = convertToStruct(sourceRecordValue.get("after"));
                if (after != null) {
                    for (Field field : after.schema().fields()) {
                        preparedStatement.setObject(index++, after.get(field));
                    }
                }
                if (operation.equals("d")) {
                    Struct before = convertToStruct(sourceRecordValue.get("before"));
                    if (before != null) {
                        preparedStatement.setObject(index, before.get("id"));
                    }
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
            throw e;
        }
    }

    private Struct convertToStruct(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }

        Schema schema = createSchema(jsonNode);

        Struct struct = new Struct(schema);
        for (Field field : schema.fields()) {
            String fieldName = field.name();
            if (jsonNode.has(fieldName)) {
                struct.put(fieldName, getValueFromJsonNode(jsonNode.get(fieldName), field.schema()));
            }
        }
        return struct;
    }

    private String generateUpdateSQL(String databaseName, String tableName, Struct after) {
        String columns = after.schema().fields().stream()
                .map(field -> field.name() + " = ?")
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s.%s SET %s WHERE id = ", databaseName, tableName, columns);
    }

    private String generateCreateSQL(String databaseName, String tableName, Struct after) {
        String columns = after.schema().fields().stream()
                .map(Field::name)
                .collect(Collectors.joining(", "));

        String valuePlaceholders = after.schema().fields().stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s.%s (%s) VALUES (%s)", databaseName, tableName, columns, valuePlaceholders);
    }

    private String generateDeleteSQL(String databaseName, String tableName) {
        return "DELETE FROM " + databaseName + "." + tableName + " WHERE id = ?";
    }
}