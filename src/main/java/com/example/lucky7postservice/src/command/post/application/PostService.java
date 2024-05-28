package com.example.lucky7postservice.src.command.post.application;

import com.example.lucky7postservice.src.command.comment.domain.Comment;
import com.example.lucky7postservice.src.command.comment.domain.Reply;
import com.example.lucky7postservice.src.command.comment.domain.repository.CommentRepository;
import com.example.lucky7postservice.src.command.post.api.dto.*;
import com.example.lucky7postservice.src.command.post.domain.*;
import com.example.lucky7postservice.src.command.post.domain.repository.HashtagRepository;
import com.example.lucky7postservice.src.command.post.domain.repository.WalletRepository;
import com.example.lucky7postservice.src.command.comment.domain.repository.ReplyRepository;
import com.example.lucky7postservice.src.command.like.domain.repository.PostLikeRepository;
import com.example.lucky7postservice.src.command.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.query.entity.blog.QueryBlog;
import com.example.lucky7postservice.src.query.repository.BlogQueryRepository;
import com.example.lucky7postservice.src.query.repository.MemberQueryRepository;
import com.example.lucky7postservice.src.query.repository.PostQueryRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.config.SetTime;
import com.example.lucky7postservice.utils.entity.State;
import com.example.lucky7postservice.utils.kafka.PostProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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

    private final MemberQueryRepository memberQueryRepository;
    private final BlogQueryRepository blogQueryRepository;
    private final PostQueryRepository postQueryRepository;

    private final PostProducer postProducer;

    public List<GetHomePostsRes> getHomePosts(int page, int pageSize) throws BaseException {
        // TODO : memberId 받아와서 적용
        Long memberId = 1L;
        memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        return postQueryRepository.findAllOrderByLikeCnt(PageRequest.of(page, pageSize));
    }

    public GetHashtagsRes getHashtags(Long blogId) throws BaseException {
        // 블로그 존재 여부 확인
        QueryBlog blog = blogQueryRepository.findByIdAndState(blogId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        // 블로그 주인장 존재 여부 확인
        memberQueryRepository.findById(blog.getMember().getId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        List<String> freeList = postQueryRepository.findAllHashtagByBlogId(blogId, PostType.FREE);
        List<String> walletList = postQueryRepository.findAllHashtagByBlogId(blogId, PostType.WALLET);

        return new GetHashtagsRes(freeList, walletList);
    }

    public GetBlogPostsRes getBlogPosts(int page, Long blogId, String hashtag) throws BaseException {
        // TODO : memberId 받아와서 적용
        Long memberId = 1L;
        memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        QueryBlog blog = blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        // 블로그 주인 존재 여부 확인
        Long blogMemberId = blog.getMember().getId();
        memberQueryRepository.findById(blogMemberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG_USER));

        List<GetPosts> posts;
        if(hashtag.equals("ALL")) {
            posts = postQueryRepository.findAllBlogPosts(blogId, PageRequest.of(page, 15));
        } else {
            posts = postQueryRepository.findAllBlogPostsWithHashtag(blogId, PageRequest.of(page, 15), hashtag);
        }

        return new GetBlogPostsRes(blog.getId(), blogMemberId,
                posts.size(), posts);
    }

    @Transactional
    public PostPostRes postPost(Long postId, PostPostReq postReq) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        Long memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        QueryBlog queryBlog = blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        Post post;
        PostType postType = postReq.getPostType().equals("FREE") ? PostType.FREE : PostType.WALLET;

        if(postId == 0) {
            post = postRepository.save(Post.of(memberId, queryBlog.getId(),
                    postType, postReq.getTitle(), postReq.getContent(), postReq.getPreview(),
                    postReq.getThumbnail(), postReq.getMainHashtag(),
                    PostState.ACTIVE));
        } else {
            // 이미 임시 저장한 글이 있다면, 불러와서 새로 저장함
            post = postRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

            post.savePost(postType, postReq.getTitle(), postReq.getContent(),
                    postReq.getPreview(), postReq.getThumbnail(), postReq.getMainHashtag());

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
                    SetTime.stringToLocalDate(wallet.getConsumedDate()), wallet.getMemo().trim(), wallet.getAmount(), wallet.getWalletType()));
        }

//        postProducer.send("post", post);

        return new PostPostRes(post.getId());
    }

    @Transactional
    public PostPostRes savePost(Long postId, PostSavedPostReq postReq) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        Long memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        QueryBlog queryBlog = blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        Post post;
        PostType postType = postReq.getPostType().equals("FREE") ? PostType.FREE : PostType.WALLET;

        if(postId == 0) {
            post = postRepository.save(Post.saveTemporaryPost(memberId, queryBlog.getId(),
                    postReq.getTitle(), postReq.getContent(), postReq.getPreview(),
                    postReq.getMainHashtag(), postType));
        } else {
            // 이미 임시 저장한 글이 있다면, 불러와서 새로 저장함
            post = postRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

            post.modifyTemporaryPost(postReq.getTitle(), postReq.getContent(),
                    postReq.getPreview(), postReq.getMainHashtag(), postType);

            // 이미 저장되어 있는 해시태그, 소비 내역을 삭제
            deleteHashtag(postId);
            deleteWallet(postId);
        }

        // 해시태그, 소비 내역을 새롭게 저장
        updateHashtag(postReq.getHashtagList(), post);
        updateWallet(postReq.getWalletList(), post);

        return new PostPostRes(post.getId());
    }

    public List<GetSavedPostsRes> getSavedPosts() throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        Long memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        return postQueryRepository.findAllTemporaryPosts(memberId);
    }

    @Transactional
    public String deletePost(Long postId) throws BaseException {
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        Long memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

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
        // TODO : 멤버 아이디 추출 후 예외 처리 적용
        Long memberId = 1L;
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 존재 여부 확인
        blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        // TODO : 대표 해시태그 적용

        // 기존 글을 불러와서 수정함
        Post post = postRepository.findByIdAndPostState(postId, PostState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));
        PostType postType = postReq.getPostType().equals("FREE") ? PostType.FREE : PostType.WALLET;

        post.savePost(postType, postReq.getTitle(), postReq.getContent(),
                postReq.getPreview(), postReq.getThumbnail(), postReq.getMainHashtag());

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
                    Wallet.of(post.getMemberId(), post,
                            SetTime.stringToLocalDate(wallet.getConsumedDate()), wallet.getMemo().trim(), wallet.getAmount(), wallet.getWalletType())
            );
        }
    }
}
