package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.GetHomePostsRes;
import com.example.lucky7postservice.src.command.post.api.dto.GetSavedPostsRes;
import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.src.command.post.domain.PostState;
import com.example.lucky7postservice.src.query.entity.post.QueryPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostQueryRepository extends JpaRepository<QueryPost, Long> {
    @Query(value = """
            select p.id as postId,\s
            p.title as title, p.thumbnail as thumbnail, p.mainHashtag as mainHashtag,\s
            p.blog.id as blogId, p.member.id as memberId, p.member.nickname as nickname,\s
            DATE_FORMAT(p.createdAt, '%d.%m.%y') as createdAt from post as p\s
            where p.postState='ACTIVE'\s
            order by (select count(l) from post_like as l where l.post.id = p.id) desc, p.id asc\s""")
    List<GetHomePostsRes> findAllOrderByLikeCnt(Pageable pageable);

    @Query(value = """
            select p.id as postId, p.title as title,\s
            DATE_FORMAT(p.updatedAt, '%d.%m.%y') as updatedAt from post as p\s
            where p.member.id=:memberId and p.postState='TEMPORARY'\s
            order by p.updatedAt desc\s""")
    List<GetSavedPostsRes> findAllTemporaryPosts(Long memberId);
}
