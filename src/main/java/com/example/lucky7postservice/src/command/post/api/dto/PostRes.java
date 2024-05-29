package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public interface PostRes {
    @Schema(description = "게시물 아이디", example = "1")
    Long getPostId();
    @Schema(description = "게시물 작성자 아이디", example = "1")
    Long getMemberId();
    @Schema(description = "게시물 작성자 닉네임", example = "조은")
    String getNickname();
    @Schema(description = "게시물 작성자 프로필 사진", example = "프로필 사진 링크")
    String getProfileImg();
    @Schema(description = "게시물 작성자 소개글", example = "나는 조은이야~")
    String getAbout();
    @Schema(description = "게시물 제목", example = "오늘의 글")
    String getTitle();
    @Schema(description = "게시물 내용", example = "")
    String getContent();
    @Schema(description = "게시물 미리보기", example = "미리보기")
    String getPreview();
    @Schema(description = "게시물 썸네일", example = "사진 절대 경로")
    String getThumbnail();
    @Schema(description = "자유글/가계부 여부", example = "FREE/WALLET")
    String getPostType();
    @Schema(description = "대표 해시태그", example = "야구")
    String getMainHashtag();
    @Schema(description = "게시물 작성일", example = "2024.04.14")
    String getCreatedAt();
    @Schema(description = "게시물 댓글 개수", example = "2")
    int getCommentCnt();
    @Schema(description = "게시물 좋아요 개수", example = "10")
    int getLikeCnt();
    @Schema(description = "나의 게시물 좋아요 여부", example = "10")
    boolean getIsLiked();
}
