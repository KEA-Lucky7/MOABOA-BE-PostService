package com.example.lucky7postservice.src.command.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @NotNull
    private String memo;
    private int amount;
    @NotNull
    private String walletType;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;

    public static Wallet of(Long memberId, Post post,
                            String memo, int amount, String walletType) {
        return Wallet.builder()
                .memberId(memberId)
                .post(post)
                .memo(memo)
                .amount(amount)
                .walletType(walletType)
                .state(State.ACTIVE)
                .build();
    }
}
