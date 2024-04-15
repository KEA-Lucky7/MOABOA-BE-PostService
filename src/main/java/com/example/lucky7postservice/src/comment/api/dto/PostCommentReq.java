package com.example.lucky7postservice.src.comment.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostCommentReq {
    @NotBlank(message="댓글을 입력해야 합니다.")
    @Schema(description = "댓글 내용", example = "정말 감명깊네요.")
    private String content;
}
