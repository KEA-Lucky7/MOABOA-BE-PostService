package com.example.lucky7postservice.src.comment.application;

import com.example.lucky7postservice.src.comment.api.dto.PostCommentReq;
import com.example.lucky7postservice.src.comment.api.dto.PostCommentRes;
import com.example.lucky7postservice.src.comment.domain.Comment;
import com.example.lucky7postservice.src.comment.domain.repository.CommentRepository;
import com.example.lucky7postservice.src.post.api.dto.PostPostReq;
import com.example.lucky7postservice.src.post.api.dto.PostPostRes;
import com.example.lucky7postservice.src.post.api.dto.WalletReq;
import com.example.lucky7postservice.src.post.domain.Hashtag;
import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import com.example.lucky7postservice.src.post.domain.Wallet;
import com.example.lucky7postservice.src.post.domain.repository.PostRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostCommentRes comment(Long postId, PostCommentReq commentReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        String content = commentReq.getContent().trim();
        Comment comment = commentRepository.save(Comment.of(1L, post, content));

        return new PostCommentRes(comment.getId());
    }
}
