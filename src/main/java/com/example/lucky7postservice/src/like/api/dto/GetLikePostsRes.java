package com.example.lucky7postservice.src.like.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetLikePostsRes {
    @Schema(description = "게시물 아이디", example = "1")
    private Long postId;
    @Schema(description = "게시물 제목", example = "제목입니다")
    private String title;
    @Schema(description = "게시물 대표 사진", example = "링크")
    private String thumbnail;
    @Schema(description = "작성자 아이디", example = "1")
    private Long memberId;
    @Schema(description = "작성자 닉네임", example = "조은")
    private String nickname;
    @Schema(description = "댓글 개수", example = "3")
    private int commentCnt;
    @Schema(description = "좋아요 개수", example = "70")
    private int likeCnt;
    @Schema(description = "게시물 작성 날짜", example = "24.05.16")
    private String createdAt;

    @Builder
    public GetLikePostsRes(Long postId, String title, String thumbnail,
                           Long memberId, String nickname,
                           int commentCnt, int likeCnt, String createdAt) {
        this.postId = postId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.memberId = memberId;
        this.nickname = nickname;
        this.commentCnt = commentCnt;
        this.likeCnt = likeCnt;
        this.createdAt = createdAt;
    }
}
