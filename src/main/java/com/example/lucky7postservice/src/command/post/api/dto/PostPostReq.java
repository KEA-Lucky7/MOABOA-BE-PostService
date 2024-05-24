package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class PostPostReq {
    @NotBlank(message="게시물 제목을 입력해야 합니다.")
    @Schema(description = "게시물 제목", example = "오늘의 글")
    private String title;
    @NotBlank(message="게시물 내용을 입력해야 합니다.")
    @Schema(description = "게시물 내용", example = "")
    private String content;
    @Schema(description = "게시물 썸네일", example = "사진 절대 경로")
    private String thumbnail;
    @NotBlank(message="자유글/가계부 여부를 입력해야 합니다.")
    @Pattern(regexp = "^(FREE|WALLET)$", message = "FREE 혹은 WALLET으로 입력해야 합니다")
    @Schema(description = "자유글/가계부 여부", example = "FREE/WALLET")
    private String postType;
    @NotBlank(message="대표 해시태그는 필수입니다.")
    @Schema(description = "대표 해시태그", example = "야구")
    private String mainHashtag;
    @Schema(description = "해시태그 리스트", example = "야구, 운동, 소비기록")
    private List<String> hashtagList;
    @Valid @Schema(description = "소비/수입 기록", example = "")
    private List<WalletReq> walletList;
}
