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
        // TODO : 멤버 존재 여부 확인, 임시저장 했던 글에 대한 처리
        Long blogId = 1L;

        Post newPost = postRepository.save(Post.of(memberId, blogId,
                postReq.getPostType(), postReq.getTitle(), postReq.getContent(), postReq.getThumbnail(),
                PostState.ACTIVE));
        Long postId = newPost.getId();

        // TODO : 이미 저장되어 있는 해시태그, 소비 내역을 어떻게 처리할 건지 생각 필요
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

    @Transactional
    public PostPostRes savePost(Long postId, Long memberId, SavePostReq postReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Long blogId = 1L;

        if(postId == 0) {
            Post newPost = postRepository.save(Post.savePost(memberId, blogId,
                    postReq.getTitle(), postReq.getContent()));
            postId = newPost.getId();
        } else {
            // 이미 임시 저장한 글이 있다면, 불러와서 새로 저장함
            Post post = postRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

            postId = post.getId();
            post.setTitle(postReq.getTitle());
            post.setContent(postReq.getContent());

            // 이미 저장되어 있는 해시태그를 삭제
            hashtagRepository.deleteAll(hashtagRepository.findAllByPostId(postId));
        }

        // 해시태그를 새롭게 저장
        for(String hashtag : postReq.getHashtagList()) {
            hashtagRepository.save(Hashtag.of(postId, hashtag.trim()));
        }

        return new PostPostRes(postId);
    }
}
