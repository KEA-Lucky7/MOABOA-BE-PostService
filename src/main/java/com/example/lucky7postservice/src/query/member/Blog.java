package com.example.lucky7postservice.src.query.member;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog extends BaseEntity {
    @NotNull
    private Long memberId;
    private String about;
    @NotNull
    private String name;
    private String headerImage;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;
}
