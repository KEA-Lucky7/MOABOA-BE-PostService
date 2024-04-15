package com.example.lucky7postservice.src.like.application;

import com.example.lucky7postservice.src.like.domain.PostLike;
import com.example.lucky7postservice.src.like.domain.repository.PostLikeRepository;
import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import com.example.lucky7postservice.src.post.domain.repository.PostRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

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
}
