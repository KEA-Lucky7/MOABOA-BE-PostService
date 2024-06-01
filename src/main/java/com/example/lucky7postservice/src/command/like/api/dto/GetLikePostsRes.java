package com.example.lucky7postservice.src.command.like.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetLikePostsRes {
    private Long memberId;
    private int postCnt;
    private List<LikePostsRes> postList;
}
