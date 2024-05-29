package com.example.lucky7postservice.src.command.post.api.dto;

public interface ReplyRes {
    Long getReplyId();
    Long getCommentId();
    String getMemberId();
    String getNickname();
    String getProfileImg();
    String getContent();
    String getCreatedAt();
}
