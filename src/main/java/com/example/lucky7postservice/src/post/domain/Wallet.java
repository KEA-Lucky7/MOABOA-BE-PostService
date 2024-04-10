package com.example.lucky7postservice.src.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Wallet of(Long memberId, Long postId,
                            String memo, int amount, String walletType) {
        return Wallet.builder()
                .memberId(memberId)
                .postId(postId)
                .memo(memo)
                .amount(amount)
                .walletType(walletType)
                .state(State.ACTIVE)
                .build();
    }
}
