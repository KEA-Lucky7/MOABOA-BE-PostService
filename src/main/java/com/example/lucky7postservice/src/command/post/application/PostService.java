package com.example.lucky7postservice.src.command.post.application;

import com.example.lucky7postservice.src.auth.AuthServiceClient;
import com.example.lucky7postservice.src.command.comment.domain.Comment;
import com.example.lucky7postservice.src.command.comment.domain.Reply;
import com.example.lucky7postservice.src.command.comment.domain.repository.CommentRepository;
import com.example.lucky7postservice.src.command.comment.domain.repository.ReplyRepository;
import com.example.lucky7postservice.src.command.like.domain.repository.PostLikeRepository;
import com.example.lucky7postservice.src.command.post.api.dto.*;
import com.example.lucky7postservice.src.command.post.domain.Hashtag;
import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.src.command.post.domain.PostState;
import com.example.lucky7postservice.src.command.post.domain.PostType;
import com.example.lucky7postservice.src.command.post.domain.repository.HashtagRepository;
import com.example.lucky7postservice.src.command.post.domain.repository.PostRepository;
import com.example.lucky7postservice.src.command.wallet.domain.Wallet;
import com.example.lucky7postservice.src.command.wallet.domain.repository.WalletRepository;
import com.example.lucky7postservice.src.query.entity.blog.QueryBlog;
import com.example.lucky7postservice.src.query.entity.post.QueryPost;
import com.example.lucky7postservice.src.query.repository.*;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.config.SetTime;
import com.example.lucky7postservice.utils.entity.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

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
    private final AuthServiceClient authServiceClient;
    private Long memberId = 1L;

    private final BlogQueryRepository blogQueryRepository;
    private final PostQueryRepository postQueryRepository;
    private final HashtagQueryRepository hashtagQueryRepository;
    private final WalletQueryRepository walletQueryRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final ReplyQueryRepository replyQueryRepository;

    private final RestTemplate restTemplate;
    @Value("${rest-template.url}")
    private String feedbackUrl;

    /* 홈 화면 글 목록 반환 (좋아요순) */
    public List<GetHomePostsRes> getHomePosts(int page, int pageSize) {
        return postQueryRepository.findAllOrderByLikeCnt(PageRequest.of(page, pageSize));
    }

    /* 블로그 해시태그 목록 조회 */
    public GetHashtagsRes getHashtags(Long blogId) throws BaseException {
        // 블로그 존재 여부 확인
        QueryBlog blog = blogValidationByBlogId(blogId);

        // 블로그 주인장 존재 여부 확인
        Long blogMemberId = blog.getMember().getId();
        memberQueryRepository.findById(blogMemberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        List<String> freeList = postQueryRepository.findAllHashtagByBlogId(blogId, PostType.FREE);
        List<String> walletList = postQueryRepository.findAllHashtagByBlogId(blogId, PostType.WALLET);

        return new GetHashtagsRes(freeList, walletList);
    }

    /* 블로그 글 목록 조회 */
    public GetBlogPostsRes getBlogPosts(int page, Long blogId, String postType, String hashtag) throws BaseException {
        // 블로그 존재 여부 확인
        QueryBlog blog = blogValidationByBlogId(blogId);

        // 블로그 주인 존재 여부 확인
        Long blogMemberId = blog.getMember().getId();
        memberQueryRepository.findById(blogMemberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG_USER));

        List<GetPosts> posts;
        if(postType.equals("ALL") && hashtag.equals("ALL")) {
            posts = postQueryRepository.findAllBlogPosts(blogId, PageRequest.of(page, 15));
        } else {
            PostType type = postType.equals("자유글") ? PostType.FREE : PostType.WALLET;
            posts = postQueryRepository.findAllBlogPostsWithHashtag(blogId, PageRequest.of(page, 15), type, hashtag);
        }

        return new GetBlogPostsRes(blog.getId(), blogMemberId,
                posts.size(), posts);
    }

    /* 글 저장 */
    @Transactional
    public PostPostRes postPost(Long postId, PostPostReq postReq) throws BaseException {
        // 멤버 예외 처리
        memberId = userValidation();

        // 블로그 예외 처리
        QueryBlog queryBlog = blogValidationByMemberId(memberId);

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

            post.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // 이미 저장되어 있는 해시태그, 소비 내역을 삭제
            deleteHashtag(postId);
            deleteWallet(postId);
        }

        // 해시태그, 소비 내역을 새롭게 저장
        updateHashtag(postReq.getHashtagList(), post);
        updateWallet(postReq.getWalletList(), post);

        // 소비 내역 피드백 생성
        createFeedback(post.getId(), postReq.getWalletList());

        return new PostPostRes(post.getId());
    }

    /* 글 임시 저장 */
    @Transactional
    public PostPostRes savePost(Long postId, PostSavedPostReq postReq) throws BaseException {
        // 멤버 예외 처리
        memberId = userValidation();

        // 블로그 예외 처리
        QueryBlog queryBlog = blogValidationByMemberId(memberId);

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

    /* 임시 저장 목록 조회 */
    public List<GetSavedPostsRes> getSavedPosts() throws BaseException {
        // 멤버 예외 처리
        memberId = userValidation();

        // 블로그 예외 처리
        blogValidationByMemberId(memberId);

        return postQueryRepository.findAllTemporaryPosts(memberId);
    }

    /* 임시 저장한 글 상세 조회 */
    public GetSavedPostRes getSavedPost(Long postId) throws BaseException {
        // 멤버 예외 처리
        memberId = userValidation();

        // 블로그 예외 처리
        blogValidationByMemberId(memberId);

        // 게시물 존재 여부 확인
        QueryPost post = postQueryRepository.findByIdAndPostState(postId, PostState.TEMPORARY)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));

        List<String> hashtagList = hashtagQueryRepository.findAllByPostId(postId);
        List<WalletsRes> walletList = walletQueryRepository.findAllByPostIdAndState(postId);

        return GetSavedPostRes.builder()
                .postId(postId)
                .memberId(memberId)
                .title(post.getTitle())
                .content(post.getContent())
                .preview(post.getPreview())
                .thumbnail(post.getThumbnail())
                .postType(post.getPostType() == PostType.FREE ? "자유글" : "소비 일기")
                .mainHashtag(post.getMainHashtag())
                .hashtagList(hashtagList)
                .walletList(walletList)
                .build();
    }

    /* 글 상세 조회 */
    public GetPostRes getPost(Long postId) throws BaseException {
        // 멤버 예외 처리
//        memberId = userValidation();

        // 게시물 존재 여부 확인
        PostRes postRes = postQueryRepository.findByPostIdAndState(postId, memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_POST));
        Long postMemberId = postRes.getMemberId();

        // 블로그 예외 처리
        blogValidationByMemberId(postMemberId);

        List<String> hashtagList = hashtagQueryRepository.findAllByPostId(postId);
        List<WalletsRes> walletList = walletQueryRepository.findAllByPostIdAndState(postId);

        List<CommentRes> commentRes = commentQueryRepository.findAllByPostIdAndState(postId);
        List<GetCommentsRes> commentList = commentRes.stream()
                .map(d -> GetCommentsRes.builder()
                        .commentId(d.getCommentId())
                        .memberId(d.getMemberId())
                        .nickname(d.getNickname())
                        .profileImg(d.getProfileImg())
                        .content(d.getContent())
                        .createdAt(d.getCreatedAt())
                        .replyList(replyQueryRepository.findAllByCommentIdAndState(d.getCommentId()))
                        .build())
                .toList();

        List<PrevPostsRes> prevPostList = postQueryRepository.findAllPrevPostByPostIdAndPostState(postId, postMemberId);
        prevPostList.addAll(postQueryRepository.findAllNextPostByPostIdAndPostState(postId, postMemberId));

        String feedback = getFeedback(postRes.getPostId());

        return GetPostRes.builder()
                .postId(postRes.getPostId())
                .memberId(postRes.getMemberId())
                .nickname(postRes.getNickname())
                .profileImg(postRes.getProfileImg())
                .about(postRes.getAbout())
                .title(postRes.getTitle())
                .content(postRes.getContent())
                .feedback(feedback)
                .preview(postRes.getPreview())
                .thumbnail(postRes.getThumbnail())
                .postType(postRes.getPostType())
                .mainHashtag(postRes.getMainHashtag())
                .createdAt(postRes.getCreatedAt())
                .commentCnt(postRes.getCommentCnt())
                .likeCnt(postRes.getLikeCnt())
                .isLiked(postRes.getIsLiked())
                .hashtagList(hashtagList)
                .walletList(walletList)
                .commentList(commentList)
                .postList(prevPostList)
                .build();
    }

    /* 글 삭제 */
    @Transactional
    public String deletePost(Long postId) throws BaseException {
        // 멤버 예외 처리
        memberId = userValidation();

        // 블로그 예외 처리
        blogValidationByMemberId(memberId);

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

    /* 글 수정 */
    @Transactional
    public PostPostRes modifyPost(Long postId, PostPostReq postReq) throws BaseException {
        // 멤버 예외 처리
        memberId = userValidation();

        // 블로그 예외 처리
        blogValidationByMemberId(memberId);

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

        // 소비 내역 피드백 생성
        createFeedback(post.getId(), postReq.getWalletList());

        return new PostPostRes(post.getId());
    }

    /* 블로그 피드백 생성 */
    public void createFeedback(Long postId, List<WalletReq> walletList) {
        List<ConsumptionReq> consumptionList = walletList.stream()
                .map(d -> ConsumptionReq.builder()
                        .name(d.getMemo())
                        .category(d.getWalletType())
                        .cost(d.getAmount())
                        .date(SetTime.dateFormat(d.getConsumedDate()))
                        .build())
                .toList();

        RequestEntity<PostFeedbackReq> req = RequestEntity
                .post(feedbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PostFeedbackReq(postId, consumptionList));

        try {
            ResponseEntity<GetFeedbackRes> res = restTemplate.exchange(req, GetFeedbackRes.class);

            if(res.getStatusCode() == HttpStatusCode.valueOf(400) || res.getStatusCode() == HttpStatusCode.valueOf(404)) {
                log.debug("피드백이 생성되지 않았습니다");
            }

            String feedback = Objects.requireNonNull(res.getBody()).getFeedback();
            log.debug(feedback);


        } catch (RestClientException exception) {
            log.debug("피드백이 제대로 생성되지 않았습니다.");
        }
    }

    /* 블로그 피드백 조회 */
    public String getFeedback(Long postId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(feedbackUrl)
                .queryParam("post_id", postId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GetFeedbackRes> res = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    GetFeedbackRes.class);

            if(res.getStatusCode() == HttpStatusCode.valueOf(400) || res.getStatusCode() == HttpStatusCode.valueOf(404)) {
                return "피드백이 생성되지 않았습니다";
            }

            String feedback = Objects.requireNonNull(res.getBody()).getFeedback();
            log.debug(feedback);

            return feedback;
        } catch (RestClientException exception) {
            log.debug("피드백이 제대로 생성되지 않았습니다.");
        }

        return "피드백이 생성되지 않았습니다";
    }

    /* 해시태그, 소비 내역 삭제 */
    private void deleteHashtag(Long postId) {
        hashtagRepository.deleteAll(hashtagRepository.findAllByPostId(postId));
    }

    private void deleteWallet(Long postId) {
        walletRepository.deleteAll(walletRepository.findAllByPostId(postId));
    }

    /* 해시태그, 소비 내역 업데이트 */
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

    /* 멤버 예외 처리 */
    public Long userValidation() throws BaseException {
        // 멤버 예외 처리
        memberId = authServiceClient.validateToken().getBody();
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        return memberId;
    }

    /* 블로그 예외 처리 */
    public QueryBlog blogValidationByBlogId(Long blogId) throws BaseException {
        return blogQueryRepository.findByIdAndState(blogId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));
    }

    public QueryBlog blogValidationByMemberId(Long memberId) throws BaseException {
        return blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));
    }
}
