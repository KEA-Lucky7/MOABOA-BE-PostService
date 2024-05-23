package com.example.lucky7postservice.src.command.comment.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PostCommentRes {
    @Schema(description = "댓글 아이디", example = "1")
    private Long commentId;

    public PostCommentRes(Long commentId) {
        this.commentId = commentId;
    }
}
