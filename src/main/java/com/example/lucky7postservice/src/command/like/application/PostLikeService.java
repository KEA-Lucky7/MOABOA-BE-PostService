package com.example.lucky7postservice.src.command.like.application;

import com.example.lucky7postservice.src.auth.AuthServiceClient;
import com.example.lucky7postservice.src.command.like.api.dto.GetLikePostsRes;
import com.example.lucky7postservice.src.command.like.api.dto.LikePostsRes;
import com.example.lucky7postservice.src.command.like.api.dto.PatchLikePostsReq;
import com.example.lucky7postservice.src.command.like.domain.PostLike;
import com.example.lucky7postservice.src.command.like.domain.repository.PostLikeRepository;
import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.src.command.post.domain.PostState;
import com.example.lucky7postservice.src.command.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.query.repository.BlogQueryRepository;
import com.example.lucky7postservice.src.query.repository.MemberQueryRepository;
import com.example.lucky7postservice.src.query.repository.PostQueryRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.entity.State;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    private final MemberQueryRepository memberQueryRepository;
    private final AuthServiceClient authServiceClient;
    private Long memberId = 1L;

    private final BlogQueryRepository blogQueryRepository;
    private final PostQueryRepository postQueryRepository;

    /* 글 공감 API */
    @Transactional
    public String like(Long postId) throws BaseException {
        // 멤버, 블로그 예외 처리
//        memberId = userAndBlogValidation();

        // 글 존재 여부 확인
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 글 좋아요 존재 여부 확인
        Optional<PostLike> like = postLikeRepository.findByPostIdAndMemberId(postId, memberId);
        if(like.isPresent()) {
            return "이미 좋아요를 눌렀습니다";
        }

        postLikeRepository.save(PostLike.of(memberId, post));

        return "좋아요를 눌렀습니다.";
    }

    /* 글 공감 취소 API */
    @Transactional
    public String dislike(Long postId) throws BaseException {
        // 멤버, 블로그 예외 처리
//        memberId = userAndBlogValidation();

        // 글 존재 여부 확인
        postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 글 좋아요 존재 여부 확인
        PostLike like = postLikeRepository.findByPostIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST_LIKE));

        // 좋아요 삭제
        postLikeRepository.delete(like);

        return "글 좋아요를 취소하였습니다.";
    }

    /* 좋아요 누른 글 목록 조회 API */
    public GetLikePostsRes getLikeList(int page) throws BaseException {
        // 멤버, 블로그 예외 처리
//        memberId = userAndBlogValidation();

        int postCnt = postQueryRepository.findByLikeOrderById(memberId);
        List<LikePostsRes> postList = postQueryRepository.findAllByLikeOrderById(memberId, PageRequest.of(page, 15));

        return new GetLikePostsRes(memberId, postCnt, postList);
    }

    /* 좋아요 누른 글 목록 좋아요 취소 API */
    @Transactional
    public String dislikeList(PatchLikePostsReq patchLikePostsReq) throws BaseException {
        // 멤버, 블로그 예외 처리
//        memberId = userAndBlogValidation();

        List<Long> postIdList = patchLikePostsReq.getPostIdList();
        for(Long postId : postIdList) {
            System.out.println(postId);
            dislike(postId);
        }

        return "글 좋아요를 모두 취소했습니다.";
    }

    /* 멤버, 블로그 예외 처리 */
    public Long userAndBlogValidation() throws BaseException {
        // 멤버 예외 처리
        memberId = authServiceClient.validateToken().getBody();
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 예외 처리
        blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        return memberId;
    }
}
