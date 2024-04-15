package com.example.lucky7postservice.src.comment.domain;

import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
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
@Entity
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @NotNull
    private String content;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;

    public static Comment of(Long memberId, Post post, String content) {
        return Comment.builder()
                .memberId(memberId)
                .post(post)
                .content(content)
                .state(State.ACTIVE)
                .build();
    }

    public void modifyComment(String content) {
        this.content = content;
    }

    public void deleteComment() {
        this.state = State.DELETE;
    }
}
