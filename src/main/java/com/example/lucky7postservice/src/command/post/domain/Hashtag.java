package com.example.lucky7postservice.src.command.post.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {
    // TODO : 대표 해시태그 적용
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @NotNull
    private String content;

    public static Hashtag of(Post post, String content) {
        return Hashtag.builder()
                .post(post)
                .content(content)
                .build();
    }
}
