package com.example.lucky7postservice.src.post.application;

import com.example.lucky7postservice.src.post.api.dto.PostPostReq;
import com.example.lucky7postservice.src.post.api.dto.PostPostRes;
import com.example.lucky7postservice.src.post.api.dto.SavePostReq;
import com.example.lucky7postservice.src.post.api.dto.WalletReq;
import com.example.lucky7postservice.src.post.domain.Hashtag;
import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import com.example.lucky7postservice.src.post.domain.Wallet;
import com.example.lucky7postservice.src.post.domain.repository.HashtagRepository;
import com.example.lucky7postservice.src.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.post.domain.repository.WalletRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public PostPostRes postPost(Long postId, Long memberId, PostPostReq postReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Long blogId = 1L;
        Post post;

        if(postId == 0) {
            post = postRepository.save(Post.of(memberId, blogId,
                    postReq.getPostType(), postReq.getTitle(), postReq.getContent(), postReq.getThumbnail(),
                    PostState.ACTIVE));
        } else {
            // 이미 임시 저장한 글이 있다면, 불러와서 새로 저장함
            post = postRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

            post.savePost(postReq.getPostType(), postReq.getTitle(), postReq.getContent(), postReq.getThumbnail());

            // 이미 저장되어 있는 해시태그를 삭제
            hashtagRepository.deleteAll(hashtagRepository.findAllByPostId(postId));
        }

        // 해시태그 저장
        for(String hashtag : postReq.getHashtagList()) {
            hashtagRepository.save(Hashtag.of(post, hashtag.trim()));
        }

        // 소비 내역 저장
        for(WalletReq wallet : postReq.getWalletList()) {
            walletRepository.save(Wallet.of(memberId, post,
                    wallet.getMemo().trim(), wallet.getAmount(), wallet.getWalletType()));
        }

        return new PostPostRes(post.getId());
    }

    @Transactional
    public PostPostRes savePost(Long postId, Long memberId, SavePostReq postReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Long blogId = 1L;
        Post post;

        if(postId == 0) {
            post = postRepository.save(Post.saveTemporaryPost(memberId, blogId,
                    postReq.getTitle(), postReq.getContent()));
        } else {
            // 이미 임시 저장한 글이 있다면, 불러와서 새로 저장함
            post = postRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

            post.modifyTemporaryPost(postReq.getTitle(), postReq.getContent());

            // 이미 저장되어 있는 해시태그를 삭제
            hashtagRepository.deleteAll(hashtagRepository.findAllByPostId(postId));
        }

        // 해시태그를 새롭게 저장
        for(String hashtag : postReq.getHashtagList()) {
            hashtagRepository.save(Hashtag.of(post, hashtag.trim()));
        }

        return new PostPostRes(post.getId());
    }

    @Transactional
    public String deletePost(Long postId) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        post.deletePost();
        return "게시물을 삭제하였습니다.";
    }
}
