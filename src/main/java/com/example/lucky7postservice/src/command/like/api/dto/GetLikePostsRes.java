package com.example.lucky7postservice.src.command.like.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public interface GetLikePostsRes {
    @Schema(description = "게시물 아이디", example = "1")
    Long getPostId();
    @Schema(description = "게시물 제목", example = "제목입니다")
    String getTitle();
    @Schema(description = "게시물 대표 사진", example = "링크")
    String getThumbnail();
    @Schema(description = "게시물 대표 해시태그", example = "야구")
    String getMainHashtag();
    @Schema(description = "작성자 블로그 아이디", example = "1")
    Long getBlogId();
    @Schema(description = "작성자 아이디", example = "1")
    Long getMemberId();
    @Schema(description = "작성자 닉네임", example = "조은")
    String getNickname();
    @Schema(description = "댓글 개수", example = "3")
    int getCommentCnt();
    @Schema(description = "좋아요 개수", example = "70")
    int getLikeCnt();
    @Schema(description = "게시물 작성 날짜", example = "24.05.16")
    String getCreatedAt();
}
