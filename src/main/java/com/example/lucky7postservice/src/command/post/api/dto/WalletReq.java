package com.example.lucky7postservice.src.command.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class WalletReq {
    @NotBlank(message="소비 내역 메모를 입력해야 합니다.")
    @Schema(description = "소비 내역 메모", example = "드럼 결제")
    private String memo;
    @Schema(description = "소비 금액", example = "100000")
    private int amount;
    @NotBlank(message="소비/수입 여부를 입력해야 합니다.")
    @Pattern(regexp = "^(income|outcome)$", message = "income 혹은 outcome으로 입력해야 합니다")
    @Schema(description = "소비/수입 여부", example = "income/outcome")
    private String walletType;
}
