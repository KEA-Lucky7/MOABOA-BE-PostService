package com.example.lucky7postservice.src.command.post.domain;

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
    @Enumerated(EnumType.STRING) @NotNull
    private PostType postType;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String thumbnail;
    @NotNull
    private String mainHashtag;
    @Enumerated(EnumType.STRING) @NotNull
    private PostState postState;

    public static Post of(Long memberId, Long blogId,
                          PostType postType, String title, String content, String thumbnail, String mainHashtag,
                          PostState state) {
        return Post.builder()
                .memberId(memberId)
                .blogId(blogId)
                .postType(postType)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .mainHashtag(mainHashtag)
                .postState(state)
                .build();
    }

    public static Post saveTemporaryPost(Long memberId, Long blogId,
                                String title, String content, PostType postType) {
        return Post.builder()
                .memberId(memberId)
                .blogId(blogId)
                .title(title)
                .content(content)
                .postType(postType)
                .postState(PostState.TEMPORARY)
                .build();
    }

    public void modifyTemporaryPost(String title, String content, PostType postType) {
        this.title = title;
        this.content = content;
        this.postType = postType;
    }

    public void savePost(PostType postType, String title, String content, String thumbnail, String mainHashtag) {
        this.postType = postType;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.mainHashtag = mainHashtag;
        this.postState = PostState.ACTIVE;
    }

    public void deletePost() {
        this.postState = PostState.DELETE;
    }
}
