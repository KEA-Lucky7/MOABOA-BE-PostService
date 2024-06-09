package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GetPostRes {
    @Schema(description = "게시물 아이디", example = "1")
    private Long postId;
    @Schema(description = "게시물 작성자 아이디", example = "1")
    private Long memberId;
    @Schema(description = "게시물 작성자 닉네임", example = "조은")
    private String nickname;
    @Schema(description = "게시물 작성자 프로필 사진", example = "프로필 사진 링크")
    private String profileImg;
    @Schema(description = "게시물 작성자 소개글", example = "나는 조은이야~")
    private String about;
    @Schema(description = "게시물 제목", example = "오늘의 글")
    private String title;
    @Schema(description = "게시물 내용", example = "")
    private String content;
    @Schema(description = "소비내역 피드백", example = "너는 소비를 정말 많이 했구나~!")
    private String feedback;
    @Schema(description = "게시물 미리보기", example = "미리보기")
    private String preview;
    @Schema(description = "게시물 썸네일", example = "사진 절대 경로")
    private String thumbnail;
    @Schema(description = "자유글/가계부 여부", example = "FREE/WALLET")
    private String postType;
    @Schema(description = "대표 해시태그", example = "야구")
    private String mainHashtag;
    @Schema(description = "게시물 작성일", example = "24.04.14")
    private String createdAt;
    @Schema(description = "게시물 댓글 개수", example = "2")
    private int commentCnt;
    @Schema(description = "게시물 좋아요 개수", example = "10")
    private int likeCnt;
    @Schema(description = "나의 게시물 좋아요 여부", example = "10")
    private Boolean isLiked;
    private List<String> hashtagList;
    private List<WalletsRes> walletList;
    private List<GetCommentsRes> commentList;
    private List<PrevPostsRes> postList;
}
