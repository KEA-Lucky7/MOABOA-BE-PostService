package com.example.lucky7postservice.src.command.comment.application;

import com.example.lucky7postservice.src.command.comment.api.dto.PostCommentReq;
import com.example.lucky7postservice.src.command.comment.api.dto.PostCommentRes;
import com.example.lucky7postservice.src.command.comment.api.dto.PostReplyReq;
import com.example.lucky7postservice.src.command.comment.api.dto.PostReplyRes;
import com.example.lucky7postservice.src.command.comment.domain.Comment;
import com.example.lucky7postservice.src.command.comment.domain.Reply;
import com.example.lucky7postservice.src.command.comment.domain.repository.CommentRepository;
import com.example.lucky7postservice.src.command.comment.domain.repository.ReplyRepository;
import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.src.command.post.domain.PostState;
import com.example.lucky7postservice.src.command.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.query.repository.MemberQueryRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.entity.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberQueryRepository memberQueryRepository;

    private Long memberId = 1L;

    /* 댓글 달기 API */
    @Transactional
    public PostCommentRes comment(Long postId, PostCommentReq commentReq) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        String content = commentReq.getContent().trim();
        Comment comment = commentRepository.save(Comment.of(memberId, post, content));

        return new PostCommentRes(comment.getId());
    }

    /* 댓글 수정 API */
    @Transactional
    public String modifyComment(Long postId, Long commentId, PostCommentReq commentReq) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        Comment comment = commentRepository.findByIdAndState(commentId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        String content = commentReq.getContent().trim();
        comment.modifyComment(content);

        return "댓글이 수정되었습니다.";
    }

    /* 댓글 삭제 API */
    @Transactional
    public String deleteComment(Long postId, Long commentId) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 존재하는 글인지 확인
        postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 존재하는 댓글인지 확인
        Comment comment = commentRepository.findByIdAndState(commentId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        comment.deleteComment();
        return "댓글이 삭제되었습니다.";
    }

    /* 답글 달기 API */
    @Transactional
    public PostReplyRes reply(Long postId, Long commentId, PostReplyReq replyReq) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 글 존재 여부 확인
        postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 존재하는 댓글인지 확인
        Comment comment = commentRepository.findByIdAndState(commentId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        String content = replyReq.getContent().trim();
        Reply reply = replyRepository.save(Reply.of(memberId, comment, content));

        return new PostReplyRes(reply.getId());
    }

    /* 답글 수정 API */
    @Transactional
    public String modifyReply(Long postId, Long commentId, Long replyId, PostReplyReq postReplyReq) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 글 존재 여부 확인
        postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 댓글 존재 여부 확인
        commentRepository.findByIdAndState(commentId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        // 답글 존재 여부 확인
        Reply reply = replyRepository.findByIdAndState(replyId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_REPLY));

        String content = postReplyReq.getContent().trim();
        reply.modifyReply(content);

        return "답글이 수정되었습니다.";
    }

    /* 답글 삭제 API */
    @Transactional
    public String deleteReply(Long postId, Long commentId, Long replyId) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 글 존재 여부 확인
        postRepository.findByIdAndPostState(postId, PostState.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 댓글 존재 여부 확인
        commentRepository.findByIdAndState(commentId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        // 답글 존재 여부 확인
        Reply reply = replyRepository.findByIdAndState(replyId, State.ACTIVE).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_REPLY));

        reply.deleteReply();
        return "답글이 성공적으로 삭제되었습니다.";
    }
}
