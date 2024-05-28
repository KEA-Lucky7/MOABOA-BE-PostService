package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.like.api.dto.GetLikePostsRes;
import com.example.lucky7postservice.src.command.post.api.dto.*;
import com.example.lucky7postservice.src.command.post.domain.PostType;
import com.example.lucky7postservice.src.query.entity.post.QueryPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostQueryRepository extends JpaRepository<QueryPost, Long> {
    @Query(value = """
            select p.id as postId,\s
            p.title as title, p.preview,\s
            p.thumbnail as thumbnail, p.mainHashtag as mainHashtag,\s
            p.blog.id as blogId, p.member.id as memberId, p.member.nickname as nickname,\s
            DATE_FORMAT(p.createdAt, '%d.%m.%y') as createdAt from post as p\s
            where p.postState='ACTIVE'\s
            order by (select count(l) from post_like as l where l.post.id = p.id) desc, p.id asc\s""")
    List<GetHomePostsRes> findAllOrderByLikeCnt(Pageable pageable);

    @Query(value = """
            select distinct p.mainHashtag as hashtag\s
            from post as p\s
            where p.blog.id=:blogId and p.postType=:postType and p.postState='ACTIVE'\s
            order by p.mainHashtag""")
    List<String> findAllHashtagByBlogId(Long blogId, PostType postType);

    @Query(value = """
            select p.id as postId, p.member.id as memberId,\s
            case\s
              when p.postType = 'FREE' then '자유글'\s
              else '소비 일기'\s
            end as postType,\s
            p.mainHashtag as mainHashtag,\s
            p.title as title, p.preview as preview, p.thumbnail as thumbnail,\s
            DATE_FORMAT(p.createdAt, '%d.%m.%y') as createdAt,\s
            count(distinct c.id) + count(distinct r.id) as commentCnt,\s
            count(l.id) as likeCnt\s
            from post as p\s
            left join comment as c on c.post.id=p.id and c.state='ACTIVE'\s
            left join reply as r on r.comment.id=c.id and r.state='ACTIVE'\s
            left join post_like as l on l.post.id=p.id\s
            where p.blog.id=:blogId and p.postState='ACTIVE' and p.mainHashtag=:hashtag\s
            group by p.id\s
            order by p.createdAt desc""")
    List<GetPosts> findAllBlogPostsWithHashtag(Long blogId, Pageable pageable, String hashtag);

    @Query(value = """
            select p.id as postId, p.member.id as memberId,\s
            case\s
              when p.postType = 'FREE' then '자유글'\s
              else '소비 일기'\s
            end as postType,\s
            p.mainHashtag as mainHashtag,\s
            p.title as title, p.preview as preview, p.thumbnail as thumbnail,\s
            DATE_FORMAT(p.createdAt, '%d.%m.%y') as createdAt,\s
            count(distinct c.id) + count(distinct r.id) as commentCnt,\s
            count(l.id) as likeCnt\s
            from post as p\s
            left join comment as c on c.post.id=p.id and c.state='ACTIVE'\s
            left join reply as r on r.comment.id=c.id and r.state='ACTIVE'\s
            left join post_like as l on l.post.id=p.id\s
            where p.blog.id=:blogId and p.postState='ACTIVE'\s
            group by p.id\s
            order by p.createdAt desc""")
    List<GetPosts> findAllBlogPosts(Long blogId, Pageable pageable);

    @Query(value = """
            select p.id as postId, p.title as title,\s
            DATE_FORMAT(p.updatedAt, '%d.%m.%y') as updatedAt from post as p\s
            where p.member.id=:memberId and p.postState='TEMPORARY'\s
            order by p.updatedAt desc\s""")
    List<GetSavedPostsRes> findAllTemporaryPosts(Long memberId);

    @Query(value = """
            select distinct p.id as postId, p.title as title, p.thumbnail as thumbnail, p.mainHashtag as mainHashtag,\s
            p.blog.id as blogId, p.member.id as memberId, p.member.nickname as nickname,\s
            count(distinct c.id) + count(distinct r.id) as commentCnt,\s
            count(distinct l.id) as likeCnt,\s
            DATE_FORMAT(p.createdAt, '%d.%m.%y') as createdAt\s
            from post as p\s
            left join post_like as l on l.post.id=p.id\s
            left join comment as c on c.post.id=p.id and c.state='ACTIVE'\s
            left join reply as r on r.comment.id=c.id and r.state='ACTIVE'\s
            where p.postState='ACTIVE' and l.member.id=:memberId\s
            group by p.id\s
            order by p.createdAt desc""")
    List<GetLikePostsRes> findAllByLikeOrderById(Long memberId, Pageable pageable);
}
