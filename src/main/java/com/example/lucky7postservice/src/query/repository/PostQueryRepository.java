package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.GetHomePostsRes;
import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.src.query.entity.post.QueryPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostQueryRepository extends JpaRepository<QueryPost, Long> {
    @Query(value = "select p.id as postId, \n" +
            "p.title as title, p.thumbnail as thumbnail, p.mainHashtag as mainHashtag, \n" +
            "p.blog.id as blogId, p.member.id as memberId, p.member.nickname as nickname, \n" +
            "DATE_FORMAT(p.createdAt, '%d.%m.%y') as createdAt from post as p \n" +
            "where p.postState='ACTIVE' \n" +
            "order by (select count(l) from post_like as l where l.post.id = p.id) desc, p.id asc ")
    List<GetHomePostsRes> findAllOrderByLikeCnt(Pageable pageable);
}
