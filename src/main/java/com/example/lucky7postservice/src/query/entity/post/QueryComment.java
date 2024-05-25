package com.example.lucky7postservice.src.query.entity.post;

import com.example.lucky7postservice.src.query.entity.member.QueryMember;
import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "comment")
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryComment extends BaseEntity {
    @ManyToOne @NotNull
    @JoinColumn(name = "member_id")
    private QueryMember member;
    @ManyToOne @NotNull
    @JoinColumn(name = "post_id")
    private QueryPost post;
    @NotNull
    private String content;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;
}
