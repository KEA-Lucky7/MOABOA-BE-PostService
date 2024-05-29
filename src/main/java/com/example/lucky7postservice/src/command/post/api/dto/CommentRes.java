package com.example.lucky7postservice.src.command.post.api.dto;

public interface CommentRes {
    Long getCommentId();
    Long getMemberId();
    String getNickname();
    String getProfileImg();
    String getContent();
    String getCreatedAt();
}
