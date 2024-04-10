package com.example.lucky7postservice.src.post.application;

import com.example.lucky7postservice.src.post.api.dto.PostPostReq;
import com.example.lucky7postservice.src.post.api.dto.PostPostRes;
import com.example.lucky7postservice.src.post.api.dto.WalletReq;
import com.example.lucky7postservice.src.post.domain.Hashtag;
import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import com.example.lucky7postservice.src.post.domain.Wallet;
import com.example.lucky7postservice.src.post.domain.repository.HashtagRepository;
import com.example.lucky7postservice.src.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.post.domain.repository.WalletRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.entity.State;
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
    public PostPostRes postPost(Long memberId, PostPostReq postReq) throws BaseException {
        // 멤버 존재 여부 확인
        Long blogId = 1L;

        Post newPost = postRepository.save(Post.of(memberId, blogId,
                postReq.getPostType(), postReq.getTitle(), postReq.getContent(), postReq.getThumbnail(),
                PostState.ACTIVE));
        Long postId = newPost.getId();

        // 해시태그 저장
        for(String hashtag : postReq.getHashtagList()) {
            hashtagRepository.save(Hashtag.of(postId, hashtag.trim()));
        }

        // 소비 내역 저장
        for(WalletReq wallet : postReq.getWalletList()) {
            walletRepository.save(Wallet.of(memberId, postId,
                    wallet.getMemo(), wallet.getAmount(), wallet.getWalletType()));
        }

        return new PostPostRes(postId);
    }
}
