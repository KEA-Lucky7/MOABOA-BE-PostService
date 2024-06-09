package com.example.lucky7postservice.src.command.post.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ConsumptionReq {
    private String name;
    private String category;
    private int cost;
    private String date;
}
