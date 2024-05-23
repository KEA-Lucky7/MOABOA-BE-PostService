package com.example.lucky7postservice.src.command.comment.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PostReplyRes {
    @Schema(description = "답글 아이디", example = "1")
    private Long replyId;

    public PostReplyRes(Long replyId) {
        this.replyId = replyId;
    }
}
