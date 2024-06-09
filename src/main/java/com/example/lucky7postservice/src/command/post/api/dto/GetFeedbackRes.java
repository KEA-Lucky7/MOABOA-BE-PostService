package com.example.lucky7postservice.src.command.post.api.dto;

import lombok.Getter;

@Getter
public class GetFeedbackRes {
    private Long id;
    private Long post_id;
    private String feedback;
}
