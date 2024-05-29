package com.example.lucky7postservice.src.command.post.api.dto;

import com.example.lucky7postservice.src.command.comment.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GetCommentsRes {
    private Long commentId;
    private Long memberId;
    private String nickname;
    private String profileImg;
    private String content;
    private String createdAt;
    private List<ReplyRes> replyList;
}
