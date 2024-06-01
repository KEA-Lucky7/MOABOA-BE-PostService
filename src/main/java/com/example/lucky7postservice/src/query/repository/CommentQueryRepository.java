package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.CommentRes;
import com.example.lucky7postservice.src.query.entity.post.QueryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentQueryRepository extends JpaRepository<QueryComment, Long> {
    @Query(value = """
            select c.id as commentId,
            c.member.id as memberId, c.member.nickname as nickname, c.member.profileImage as profileImg,
            c.content as content,\s
            DATE_FORMAT(c.createdAt, '%y.%m.%d %H:%i') as createdAt
            from comment as c\s
            where c.post.id=:postId and c.state='ACTIVE'""")
    List<CommentRes> findAllByPostIdAndState(@Param("postId") Long postId);
}
