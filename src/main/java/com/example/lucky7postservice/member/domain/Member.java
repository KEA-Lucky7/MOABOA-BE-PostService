package com.example.lucky7postservice.member.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @NotNull
    private Date birth;
    private String about;
    @NotNull
    private String nickname;
    private String profileImage;
    @NotNull
    private String socialId;
    @NotNull
    private String socialType;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;
}
