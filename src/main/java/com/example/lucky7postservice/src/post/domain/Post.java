package com.example.lucky7postservice.src.post.domain;

import com.example.lucky7postservice.utils.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Post extends BaseEntity {
    @NotNull
    private Long memberId;
    @NotNull
    private Long blogId;
    private String postType;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String thumbnail;
    @Enumerated(EnumType.STRING) @NotNull
    private PostState postState;

    public static Post of(Long memberId, Long blogId,
                          String postType, String title, String content, String thumbnail,
                          PostState state) {
        return Post.builder()
                .memberId(memberId)
                .blogId(blogId)
                .postType(postType)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .postState(state)
                .build();
    }

    public static Post saveTemporaryPost(Long memberId, Long blogId,
                                String title, String content) {
        return Post.builder()
                .memberId(memberId)
                .blogId(blogId)
                .title(title)
                .content(content)
                .postState(PostState.TEMPORARY)
                .build();
    }

    public void modifyTemporaryPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void savePost(String postType, String title, String content, String thumbnail) {
        this.postType = postType;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.postState = PostState.ACTIVE;
    }

    public void deletePost() {
        this.postState = PostState.DELETE;
    }
}
