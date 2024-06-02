package com.example.lucky7postservice.utils.debezium;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class KafkaTopicService {
    private final KafkaAdmin kafkaAdmin;

    @PostConstruct
    public void init() {
        listTopics();
    }

    public void listTopics() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> topicNames = topicsResult.names().get();
            System.out.println("Available topics: " + topicNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


