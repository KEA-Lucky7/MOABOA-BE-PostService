package com.example.lucky7postservice.src.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.List;

@Getter
public class SavePostReq {
    @NotBlank(message="게시물 제목을 입력해야 합니다.")
    @Schema(description = "게시물 제목", example = "오늘의 글")
    private String title;
    @NotBlank(message="게시물 내용을 입력해야 합니다.")
    @Schema(description = "게시물 내용", example = "")
    private String content;
    @Schema(description = "해시태그 리스트", example = "야구, 운동, 소비기록")
    private List<String> hashtagList;
}
