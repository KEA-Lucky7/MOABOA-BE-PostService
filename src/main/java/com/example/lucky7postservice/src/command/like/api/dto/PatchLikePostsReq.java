package com.example.lucky7postservice.src.command.like.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class PatchLikePostsReq {
    @Schema(description = "게시물 아이디 리스트", example = "[1, 2, 3, 4]")
    private List<Long> postIdList;
}
