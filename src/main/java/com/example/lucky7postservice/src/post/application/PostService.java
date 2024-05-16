package com.example.lucky7postservice.src.post.application;

import com.example.lucky7postservice.src.comment.domain.Comment;
import com.example.lucky7postservice.src.comment.domain.Reply;
import com.example.lucky7postservice.src.comment.domain.repository.CommentRepository;
import com.example.lucky7postservice.src.comment.domain.repository.ReplyRepository;
import com.example.lucky7postservice.src.like.domain.repository.PostLikeRepository;
import com.example.lucky7postservice.src.post.api.dto.*;
import com.example.lucky7postservice.src.post.domain.Hashtag;
import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import com.example.lucky7postservice.src.post.domain.Wallet;
import com.example.lucky7postservice.src.post.domain.repository.HashtagRepository;
import com.example.lucky7postservice.src.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.post.domain.repository.WalletRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.config.SetTime;
import com.example.lucky7postservice.utils.entity.State;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final WalletRepository walletRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final PostLikeRepository likeRepository;

    public List<GetHomePostsRes> getHomePosts(int page, int pageSize) {
        // TODO : 멤버 정보 불러오기
        Long memberId = 1L;
        String nickname = "joeun";

        List<Post> postList = postRepository.findAllOrderByLikeCnt(PageRequest.of(page, pageSize));

        return postList.stream()
                .map(d -> GetHomePostsRes.builder()
                        .postId(d.getId())
                        .title(d.getTitle())
                        .thumbnail(d.getThumbnail())
                        .memberId(memberId)
                        .nickname(nickname)
                        .createdAt(SetTime.timestampToString(d.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PostPostRes postPost(Long postId, PostPostReq postReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Long memberId = 1L;

        // TODO : 블로그 존재 여부 확인 (근데 유저가 있는데 블로그가 없을 수 있나?)
        Long blogId = 1L;

        // TODO : 대표 해시태그 적용

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
    public PostPostRes savePost(Long postId, PostSavedPostReq postReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인
        Long memberId = 1L;

        // TODO : 블로그 존재 여부 확인 (근데 유저가 있는데 블로그가 없을 수 있나?)
        Long blogId = 1L;

        // TODO : 대표 해시태그 적용

        Post post;

        if(postId == 0) {
            post = postRepository.save(Post.saveTemporaryPost(memberId, blogId,
                    postReq.getTitle(), postReq.getContent(), postReq.getPostType()));
        } else {
            // 이미 임시 저장한 글이 있다면, 불러와서 새로 저장함
            post = postRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

            post.modifyTemporaryPost(postReq.getTitle(), postReq.getContent(), postReq.getPostType());

            // 이미 저장되어 있는 해시태그, 소비 내역을 삭제
            deleteHashtag(postId);
            deleteWallet(postId);
        }

        // 해시태그, 소비 내역을 새롭게 저장
        updateHashtag(postReq.getHashtagList(), post);
        updateWallet(postReq.getWalletList(), post);

        return new PostPostRes(post.getId());
    }

    public List<GetSavedPostsRes> getSavedPosts() {
        // TODO : 멤버 존재 여부 확인
        Long memberId = 1L;

        List<Post> postList = postRepository.findAllByMemberIdAndPostState(memberId, PostState.TEMPORARY);

        return postList.stream()
                .map(d -> GetSavedPostsRes.builder()
                        .postId(d.getId())
                        .title(d.getTitle())
                        .updatedAt(SetTime.timestampToString(d.getUpdatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public String deletePost(Long postId) throws BaseException {
        // TODO : 멤버 존재 여부 확인

        // 게시물 존재 여부 확인
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        // 게시물 삭제
        post.deletePost();

        // 관련 댓글 & 답글 삭제
        List<Comment> commentList = commentRepository.findAllByPostIdAndState(postId, State.ACTIVE);
        for(Comment comment : commentList) {
            comment.deleteComment();
        }

        List<Reply> replyList = replyRepository.findAllByCommentPostIdAndState(postId, State.ACTIVE);
        for(Reply reply : replyList) {
            reply.deleteReply();
        }

        // 관련 좋아요 삭제
        likeRepository.deleteAll(likeRepository.findAllByPostId(postId));

        return "게시물을 삭제하였습니다.";
    }

    @Transactional
    public PostPostRes modifyPost(Long postId, PostPostReq postReq) throws BaseException {
        // TODO : 멤버 존재 여부 확인

        // TODO : 대표 해시태그 적용

        // 기존 글을 불러와서 수정함
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        post.savePost(postReq.getPostType(), postReq.getTitle(), postReq.getContent(), postReq.getThumbnail());

        // 이미 저장되어 있는 해시태그, 소비 내역을 삭제한다
        deleteHashtag(postId);
        deleteWallet(postId);

        // 새로운 해시태그, 소비 내역을 저장한다
        updateHashtag(postReq.getHashtagList(), post);
        updateWallet(postReq.getWalletList(), post);

        return new PostPostRes(post.getId());
    }

    private void deleteHashtag(Long postId) {
        hashtagRepository.deleteAll(hashtagRepository.findAllByPostId(postId));
    }

    private void deleteWallet(Long postId) {
        walletRepository.deleteAll(walletRepository.findAllByPostId(postId));
    }

    private void updateHashtag(List<String> hashtagList, Post post) {
        // 새로운 해시태그 저장
        for(String hashtag : hashtagList) {
            hashtagRepository.save(Hashtag.of(post, hashtag.trim()));
        }
    }

    private void updateWallet(List<WalletReq> walletList, Post post) {
        // 새로운 소비 내역 저장
        for(WalletReq wallet : walletList) {
            walletRepository.save(
                    Wallet.of(post.getMemberId(), post, wallet.getMemo().trim(), wallet.getAmount(), wallet.getWalletType())
            );
        }
    }
}
