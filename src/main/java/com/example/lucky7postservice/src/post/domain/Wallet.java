package com.example.lucky7postservice.src.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import jakarta.validation.constraints.NotNull;

public class Wallet extends BaseEntity {
    @NotNull
    private Long memberId;
    @NotNull
    private Long postId;
    @NotNull
    private String memo;
    private int amount;
    @NotNull
    private String walletType;
}
