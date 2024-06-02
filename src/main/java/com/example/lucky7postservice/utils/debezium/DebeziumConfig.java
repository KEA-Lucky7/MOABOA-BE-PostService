package com.example.lucky7postservice.utils.debezium;

import java.io.File;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import io.debezium.config.Configuration;

@org.springframework.context.annotation.Configuration
public class DebeziumConfig {
    @Bean
    public Configuration mariadbConnector() throws IOException {
        File offsetStorageTempFile = File.createTempFile("offsets_", ".dat");

        return Configuration.create()
                .with("name", "test-mariadb")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")
                .with("topic.prefix", "test-mariadb-connector")
                .with("database.hostname", "localhost")
                .with("database.port", "3306")
                .with("database.user", "root")
                .with("database.password", "1234")
                .with("database.server.id", "85744")
                .with("database.server.name", "debezium-test")
                .with("database.include.list", "moaboa_command")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/tmp/dbhistory.dat")
                .with("database.history.kafka.bootstrap.servers", "localhost:9092")
                .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
                .with("schema.history.internal.kafka.topic", "test-mariadb-connector")
                .build();
    }
}
