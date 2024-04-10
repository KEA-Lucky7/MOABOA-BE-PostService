package com.example.lucky7postservice.src.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import static jakarta.persistence.GenerationType.IDENTITY;

public class HashTag {
    @Id
    @GeneratedValue(strategy = IDENTITY) @NotNull
    private Long id;
    @NotNull
    private Long postId;
    @NotNull
    private String content;
}
