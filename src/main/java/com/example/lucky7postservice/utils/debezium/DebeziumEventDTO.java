package com.example.lucky7postservice.utils.debezium;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DebeziumEventDTO {
    private String op;
    private Map<String, Object> before;
    private Map<String, Object> after;
    private Map<String, Object> source;
    private Long ts_ms;
    private Object transaction;
}
