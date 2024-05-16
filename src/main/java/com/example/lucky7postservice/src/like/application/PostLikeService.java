package com.example.lucky7postservice.src.like.application;

import com.example.lucky7postservice.src.comment.domain.Comment;
import com.example.lucky7postservice.src.comment.domain.Reply;
import com.example.lucky7postservice.src.comment.domain.repository.CommentRepository;
import com.example.lucky7postservice.src.comment.domain.repository.ReplyRepository;
import com.example.lucky7postservice.src.like.api.dto.GetLikePostsRes;
import com.example.lucky7postservice.src.like.domain.PostLike;
import com.example.lucky7postservice.src.like.domain.repository.PostLikeRepository;
import com.example.lucky7postservice.src.post.api.dto.GetHomePostsRes;
import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import com.example.lucky7postservice.src.post.domain.repository.PostRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.config.SetTime;
import com.example.lucky7postservice.utils.entity.State;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public String like(Long postId) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        // 글 존재 여부 확인
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 글 좋아요 존재 여부 확인
        Optional<PostLike> like = postLikeRepository.findByPostIdAndMemberId(postId, 1L);
        if(like.isPresent()) {
            return "이미 좋아요를 눌렀습니다";
        }

        Long memberId = 1L;
        postLikeRepository.save(PostLike.of(memberId, post));

        return "좋아요를 눌렀습니다.";
    }

    @Transactional
    public String dislike(Long postId) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        // 글 존재 여부 확인
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 글 좋아요 존재 여부 확인
        PostLike like = postLikeRepository.findByPostIdAndMemberId(postId, 1L)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST_LIKE));

        // 좋아요 삭제
        postLikeRepository.delete(like);

        return "글 좋아요를 취소하였습니다.";
    }

    public List<GetLikePostsRes> getLikeList(int page) {
        // TODO : 멤버 정보 불러오기
        Long memberId = 1L;
        String nickname = "joeun";

        // TODO : DB 연결되면 빌더가 아닌 쿼리로 한 번에 가져오기
        List<Post> postList = postRepository.findAllByLikeOrderById(PageRequest.of(page, 15));

        List<GetLikePostsRes> likePostList = new ArrayList<>();
        for(Post post : postList) {
            List<PostLike> postLikeList = postLikeRepository.findAllByPostId(post.getId());
            List<Comment> commentList = commentRepository.findAllByPostIdAndState(post.getId(), State.ACTIVE);

            int commentCnt = commentList.size();
            for(Comment comment : commentList) {
                List<Reply> replyList = replyRepository.findAllByCommentIdAndState(comment.getId(), State.ACTIVE);
                commentCnt += replyList.size();
            }

            likePostList.add(new GetLikePostsRes(post.getId(), post.getTitle(), post.getThumbnail(),
                    memberId, nickname, commentCnt, postLikeList.size(), SetTime.timestampToString(post.getCreatedAt())));
        }

        return likePostList;
    }
}
