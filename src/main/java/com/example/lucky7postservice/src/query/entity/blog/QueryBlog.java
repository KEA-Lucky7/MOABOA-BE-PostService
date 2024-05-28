package com.example.lucky7postservice.src.query.entity.blog;

import com.example.lucky7postservice.src.query.entity.member.QueryMember;
import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "blog")
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryBlog extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "member_id")
    private QueryMember member;
    private String about;
    @NotNull
    private String name;
    private String headerImage;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;
}
