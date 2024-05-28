package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public interface GetSavedPostsRes {
    @Schema(description = "게시물 아이디", example = "1")
    Long getPostId();
    @Schema(description = "게시물 제목", example = "제목입니다~")

    String getTitle();
    @Schema(description = "마지막 업데이트 날짜", example = "24.04.23")
    String getUpdatedAt();
}
