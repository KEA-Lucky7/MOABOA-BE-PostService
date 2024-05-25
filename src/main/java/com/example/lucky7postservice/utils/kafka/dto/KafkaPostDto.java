package com.example.lucky7postservice.utils.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaPostDto implements Serializable {
    private Schema schema;
    private PostPayload payload;
}
