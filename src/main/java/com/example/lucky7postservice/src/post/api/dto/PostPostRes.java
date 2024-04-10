package com.example.lucky7postservice.src.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostPostRes {
    @Schema(description = "게시물 아이디", example = "1")
    private Long postId;

    public PostPostRes(Long postId) {
        this.postId = postId;
    }
}
