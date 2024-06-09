package com.example.lucky7postservice.src.command.post.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostFeedbackReq {
    private Long post_id;
    List<ConsumptionReq> consumption_history;
}
