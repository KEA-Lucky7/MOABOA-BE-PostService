package com.example.lucky7postservice.src.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import com.example.lucky7postservice.utils.entity.State;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @NotNull
    private Long memberId;
    @NotNull
    private Long blogId;
    @NotNull
    private String postType;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String thumbnail;

    public static Post of(Long memberId, Long blogId,
                          String postType, String title, String content, String thumbnail) {
        return Post.builder()
                .memberId(memberId)
                .blogId(blogId)
                .postType(postType)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .state(State.ACTIVE)
                .build();
    }
}
