package com.example.lucky7postservice.src.comment.domain;

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

@Entity
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @NotNull
    private String content;
    @Enumerated(EnumType.STRING) @NotNull
    private State state;

    public static Reply of(Long memberId, Comment comment, String content) {
        return Reply.builder()
                .memberId(memberId)
                .comment(comment)
                .content(content)
                .state(State.ACTIVE)
                .build();
    }

    public void modifyReply(String content) {
        this.content = content;
    }
}
