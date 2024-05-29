package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GetSavedPostRes {
    @Schema(description = "게시물 아이디", example = "1")
    private Long postId;
    @Schema(description = "게시물 작성자 아이디", example = "1")
    private Long memberId;
    @Schema(description = "게시물 제목", example = "오늘의 글")
    private String title;
    @Schema(description = "게시물 내용", example = "")
    private String content;
    @Schema(description = "게시물 미리보기", example = "미리보기")
    private String preview;
    @Schema(description = "게시물 썸네일", example = "사진 절대 경로")
    private String thumbnail;
    @Schema(description = "자유글/가계부 여부", example = "FREE/WALLET")
    private String postType;
    @Schema(description = "대표 해시태그", example = "야구")
    private String mainHashtag;
    @Schema(description = "해시태그 리스트", example = "야구, 운동, 소비기록")
    private List<String> hashtagList;
    @Schema(description = "소비/수입 기록", example = "")
    private List<WalletsRes> walletList;
}
