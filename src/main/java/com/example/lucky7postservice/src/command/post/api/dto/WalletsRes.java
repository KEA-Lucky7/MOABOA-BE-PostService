package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public interface WalletsRes {
    @Schema(description = "소비 내역 날짜", example = "2024.05.24")
    String getConsumedDate();
    @Schema(description = "소비 내역 메모", example = "드럼 결제")
    String getMemo();
    @Schema(description = "소비 금액", example = "100000")
    int getAmount();
    @Schema(description = "소비 태그", example = "FOOD, TRAFFIC, LEISURE, EDUCATION, LIFE, FINANCE")
    String getWalletType();
}
