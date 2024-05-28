package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public interface GetPosts {
    @Schema(description = "게시물 아이디", example = "1")
    Long getPostId();
    @Schema(description = "작성자 아이디", example = "1")
    Long getMemberId();
    @Schema(description = "자유글/가계부 여부", example = "자유글/소비일기")
    String getPostType();
    @Schema(description = "게시물 대표 해시태그", example = "야구")
    String getMainHashtag();
    @Schema(description = "게시물 제목", example = "제목입니다")
    String getTitle();
    @Schema(description = "게시물 글 미리보기", example = "와랄라")
    String getPreview();
    @Schema(description = "게시물 대표 사진", example = "링크")
    String getThumbnail();
    @Schema(description = "게시물 작성 날짜", example = "24.05.16")
    String getCreatedAt();
    @Schema(description = "댓글 개수", example = "3")
    int getCommentCnt();
    @Schema(description = "좋아요 개수", example = "70")
    int getLikeCnt();
}
