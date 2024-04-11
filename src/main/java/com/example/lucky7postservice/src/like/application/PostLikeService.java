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

        Long memberId = 1L;
        postLikeRepository.save(PostLike.of(memberId, post));

        return "좋아요를 눌렀습니다.";
    }
}
