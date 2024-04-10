package com.example.lucky7postservice.src.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import jakarta.validation.constraints.NotNull;

public class Post extends BaseEntity {
    @NotNull
    private Long memberId;
    @NotNull
    private Long blogId;
    @NotNull
    private String postType;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String thumbnail;
}
