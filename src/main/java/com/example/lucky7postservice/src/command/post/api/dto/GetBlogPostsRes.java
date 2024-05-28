package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class GetBlogPostsRes {
    @Schema(description = "블로그 아이디", example = "1")
    private Long blogId;
    @Schema(description = "블로그 주인 아이디", example = "1")
    private Long memberId;
    @Schema(description = "블로그 글 개수", example = "10")
    private int postCnt;
    @Schema(description = "블로그 글 목록", example = "")
    private List<GetPosts> postList;

    public GetBlogPostsRes(Long blogId, Long memberId, int postCnt, List<GetPosts> postList) {
        this.blogId = blogId;
        this.memberId = memberId;
        this.postCnt = postCnt;
        this.postList = postList;
    }
}
