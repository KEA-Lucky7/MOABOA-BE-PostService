package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetHomePostsRes {
    @Schema(description = "게시물 아이디", example = "1")
    private Long postId;
    @Schema(description = "게시물 제목", example = "제목입니다")
    private String title;
    @Schema(description = "게시물 대표 사진", example = "링크")
    private String thumbnail;
    @Schema(description = "게시물 대표 해시태그", example = "야구")
    private String mainHashtag;
    @Schema(description = "작성자 블로그 아이디", example = "1")
    private Long blogId;
    @Schema(description = "작성자 아이디", example = "1")
    private Long memberId;
    @Schema(description = "작성자 닉네임", example = "조은")
    private String nickname;
    @Schema(description = "게시물 작성 날짜", example = "24.05.16")
    private String createdAt;

    @Builder
    public GetHomePostsRes(Long postId, String title, String thumbnail, String mainHashtag,
                           Long blogId, Long memberId, String nickname, String createdAt) {
        this.postId = postId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.mainHashtag = mainHashtag;
        this.blogId = blogId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
