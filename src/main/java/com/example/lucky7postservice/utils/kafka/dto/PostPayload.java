package com.example.lucky7postservice.utils.kafka.dto;

import com.example.lucky7postservice.src.command.post.domain.PostState;
import com.example.lucky7postservice.src.command.post.domain.PostType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Time;
import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Builder
public class PostPayload {
    private Long id;
    private Long member_id;
    private Long blog_id;
    @Enumerated(EnumType.STRING)
    private PostType post_type;
    private String title;
    private String content;
    private String thumbnail;
    private String main_hashtag;
    @Enumerated(EnumType.STRING)
    private PostState post_state;
    private Timestamp created_at;
    private Timestamp updated_at;

    @Builder
    public static PostPayload of(Long id, Long memberId, Long blogId,
                                 PostType postType, String title, String content,
                                 String thumbnail, String mainHashtag, PostState postState,
                                 Timestamp created_at, Timestamp updated_at) {
        return PostPayload.builder()
                .id(id)
                .member_id(memberId)
                .blog_id(blogId)
                .post_type(postType)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .main_hashtag(mainHashtag)
                .post_state(postState)
                .created_at(created_at)
                .updated_at(updated_at)
                .build();
    }
}
