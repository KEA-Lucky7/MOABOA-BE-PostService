package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public interface PrevPostsRes {
    @Schema(description = "게시물 아이디", example = "1")
    Long getPostId();
    @Schema(description = "작성자 아이디", example = "1")
    Long getMemberId();
    @Schema(description = "게시물 제목", example = "제목입니다")
    String getTitle();
    @Schema(description = "게시물 작성 날짜", example = "24.05.16")
    String getCreatedAt();
    @Schema(description = "댓글 개수", example = "3")
    int getCommentCnt();
}
