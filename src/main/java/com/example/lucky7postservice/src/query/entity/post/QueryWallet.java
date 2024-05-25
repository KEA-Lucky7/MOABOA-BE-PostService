package com.example.lucky7postservice.src.query.entity.post;

import com.example.lucky7postservice.src.query.entity.member.QueryMember;
import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity(name = "wallet")
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryWallet extends BaseEntity {
    @ManyToOne @NotNull
    @JoinColumn(name = "member_id")
    private QueryMember member;
    @ManyToOne @NotNull
    @JoinColumn(name = "post_id")
    private QueryPost post;
    @NotNull
    private LocalDate consumedDate;
    @NotNull
    private String memo;
    private int amount;
    @NotNull
    private String walletType;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;
}
