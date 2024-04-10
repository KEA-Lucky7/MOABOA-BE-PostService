package com.example.lucky7postservice.src.post.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
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
    @CreatedDate
    private Timestamp createdAt;
    @LastModifiedDate
    private Timestamp updatedAt;
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

    public static Post savePost(Long memberId, Long blogId,
                                String title, String content) {
        return Post.builder()
                .memberId(memberId)
                .blogId(blogId)
                .title(title)
                .content(content)
                .postState(PostState.TEMPORARY)
                .build();
    }
}
