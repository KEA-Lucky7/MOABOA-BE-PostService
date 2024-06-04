package com.example.lucky7postservice.utils.debezium;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DebeziumSourceEventListener {
    private final Executor executor;
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${debezium.history.kafka.topic}")
    private String kafkaTopic;

    public DebeziumSourceEventListener(Configuration mariadbConnector, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.executor = Executors.newSingleThreadExecutor();
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(mariadbConnector.asProperties())
                .notifying(this::handleChangeEvent)
                .build();
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        Struct sourceRecordKey = (Struct) sourceRecord.key();
        Struct sourceRecordValue = (Struct) sourceRecord.value();

        log.info("Key = '" + sourceRecordKey + "' value = '" + sourceRecordValue + "'");

        // Convert Struct to Map
        Map<String, Object> keyMap = convertStructToMap(sourceRecordKey);
        Map<String, Object> valueMap = convertStructToMap(sourceRecordValue);

        if(valueMap == null) {
            return;
        }

        try {
            // Convert Map to JSON string
            String key = objectMapper.writeValueAsString(keyMap);
            String value = objectMapper.writeValueAsString(valueMap);

            kafkaTemplate.send(kafkaTopic, key, value);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert struct to JSON", e);
        }
    }

    private Map<String, Object> convertStructToMap(Struct struct) {
        if(struct == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        for (Field field : struct.schema().fields()) {
            Object value = struct.get(field);
            if (value instanceof Struct) {
                value = convertStructToMap((Struct) value);
            } else if (field.name().equals("created_at") || field.name().equals("updated_at")) {
                value = convertIso8601ToFormattedString((String) value);
            } else if (field.name().equals("consumed_date") && value instanceof Number) {
                value = convertConsumedDate((Number) value);
            }
            map.put(field.name(), value);
        }
        return map;
    }

    private String convertIso8601ToFormattedString(String isoDateTime) {
        Instant instant = Instant.parse(isoDateTime);
        LocalDateTime kstTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return kstTime.format(formatter);
    }

    private String convertConsumedDate(Number consumedDate) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(consumedDate.longValue() * 24 * 60 * 60, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }
}