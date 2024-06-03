package com.example.lucky7postservice.utils.debezium;

import io.debezium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Slf4j
@org.springframework.context.annotation.Configuration
public class DebeziumConfig {

    @Value("${debezium.name}")
    private String name;
    @Value("${debezium.connector.class}")
    private String connectorClass;
    @Value("${debezium.offset.storage}")
    private String offsetStorage;
    @Value("${debezium.offset.file.filename}")
    private String offsetStorageFilename;
    @Value("${debezium.offset.flush.interval}")
    private int offsetFlushIntervalMs;
    @Value("${debezium.topic.prefix}")
    private String topicPrefix;
    @Value("${debezium.database.hostname}")
    private String databaseHostname;
    @Value("${debezium.database.port}")
    private int databasePort;
    @Value("${debezium.database.user}")
    private String databaseUser;
    @Value("${debezium.database.password}")
    private String databasePassword;
    @Value("${debezium.database.server.id}")
    private int databaseServerId;
    @Value("${debezium.database.server.name}")
    private String databaseServerName;
    @Value("${debezium.database.include.list}")
    private String databaseIncludeList;
    @Value("${debezium.history.file.filename}")
    private String historyFileFilename;
    @Value("${debezium.history.kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;
    @Value("${debezium.history.kafka.topic}")
    private String kafkaTopic;

    @Bean
    public Configuration mariadbConnector() {
        log.info("KafkaBootstrapServer : {}", kafkaBootstrapServers);

        return Configuration.create()
                .with("name", name)
                .with("connector.class", connectorClass)
                .with("offset.storage", offsetStorage)
                .with("offset.storage.file.filename", offsetStorageFilename)
                .with("offset.flush.interval.ms", offsetFlushIntervalMs)
                .with("topic.prefix", topicPrefix)
                .with("database.hostname", databaseHostname)
                .with("database.port", databasePort)
                .with("database.user", databaseUser)
                .with("database.password", databasePassword)
                .with("database.server.id", databaseServerId)
                .with("database.server.name", databaseServerName)
                .with("database.include.list", databaseIncludeList)
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", historyFileFilename)
                .with("database.history.kafka.bootstrap.servers", kafkaBootstrapServers)
                .with("schema.history.internal.kafka.bootstrap.servers", kafkaBootstrapServers)
                .with("schema.history.internal.kafka.topic", kafkaTopic)
                .with("database.history.recovery.mode", "schema_only_recovery")
                .build();
    }
}