package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public interface PostRes {
    @Schema(description = "게시물 아이디", example = "1")
    Long getPostId();
    @Schema(description = "게시물 작성자 아이디", example = "1")
    Long getMemberId();
    @Schema(description = "게시물 제목", example = "오늘의 글")
    String getTitle();
    @Schema(description = "게시물 내용", example = "")
    String getContent();
    @Schema(description = "게시물 미리보기", example = "미리보기")
    String getPreview();
    @Schema(description = "게시물 썸네일", example = "사진 절대 경로")
    String getThumbnail();
    @Pattern(regexp = "^(FREE|WALLET)$", message = "FREE 혹은 WALLET으로 입력해야 합니다")
    @Schema(description = "자유글/가계부 여부", example = "FREE/WALLET")
    String getPostType();
    @Schema(description = "대표 해시태그", example = "야구")
    String getMainHashtag();
}
