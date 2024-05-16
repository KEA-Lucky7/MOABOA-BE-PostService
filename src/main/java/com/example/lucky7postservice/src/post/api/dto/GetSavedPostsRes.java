package com.example.lucky7postservice.src.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetSavedPostsRes {
    @Schema(description = "게시물 아이디", example = "1")
    private Long postId;
    @Schema(description = "게시물 제목", example = "제목입니다~")

    private String title;
    @Schema(description = "마지막 업데이트 날짜", example = "24.04.23")
    private String updatedAt;

    @Builder
    public GetSavedPostsRes(Long postId, String title, String updatedAt) {
        this.postId = postId;
        this.title = title;
        this.updatedAt = updatedAt;
    }
}
