package com.example.lucky7postservice.src.query.entity.post;

import com.example.lucky7postservice.src.command.post.domain.PostState;
import com.example.lucky7postservice.src.command.post.domain.PostType;
import com.example.lucky7postservice.src.query.entity.blog.QueryBlog;
import com.example.lucky7postservice.src.query.entity.member.QueryMember;
import com.example.lucky7postservice.utils.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "post")
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryPost extends BaseEntity {
    @ManyToOne @NotNull
    @JoinColumn(name = "member_id")
    private QueryMember member;
    @ManyToOne @NotNull
    @JoinColumn(name = "blog_id")
    private QueryBlog blog;
    @Enumerated(EnumType.STRING) @NotNull
    private PostType postType;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String preview;
    private String thumbnail;
    @NotNull
    private String mainHashtag;
    @Enumerated(EnumType.STRING) @NotNull
    private PostState postState;
}
