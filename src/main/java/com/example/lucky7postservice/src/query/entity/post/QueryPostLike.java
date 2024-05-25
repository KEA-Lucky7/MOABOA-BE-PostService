package com.example.lucky7postservice.src.query.entity.post;

import com.example.lucky7postservice.src.query.entity.member.QueryMember;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;
@Entity(name = "post_like")
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryPostLike {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne @NotNull
    @JoinColumn(name = "member_id")
    private QueryMember member;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private QueryPost post;
    @CreatedDate
    private Timestamp createdAt;
}
