package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class WalletReq {
    @NotBlank(message="소비 내역 날짜를 입력해야 합니다.")
    @Schema(description = "소비 내역 날짜", example = "2024.05.24")
    private String consumedDate;
    @NotBlank(message="소비 내역 메모를 입력해야 합니다.")
    @Schema(description = "소비 내역 메모", example = "드럼 결제")
    private String memo;
    @Schema(description = "소비 금액", example = "100000")
    private int amount;
    @NotBlank(message="소비/수입 여부를 입력해야 합니다.")
    @Pattern(regexp = "^(FOOD|TRAFFIC|LEISURE|EDUCATION|LIFE|FINANCE)$", message = "FOOD, TRAFFIC, LEISURE, EDUCATION, LIFE, FINANCE만 가능합니다")
    @Schema(description = "소비 태그", example = "FOOD, TRAFFIC, LEISURE, EDUCATION, LIFE, FINANCE")
    private String walletType;
}
