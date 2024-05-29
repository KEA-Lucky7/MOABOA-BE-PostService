package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.comment.domain.Comment;
import com.example.lucky7postservice.src.command.post.api.dto.CommentRes;
import com.example.lucky7postservice.src.query.entity.post.QueryComment;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentQueryRepository extends JpaRepository<QueryComment, Long> {
    @Query(value = """
            select c.id as commentId,
            c.member.id as memberId, c.member.nickname as nickname, c.member.profileImage as profileImg,
            c.content as content,\s
            DATE_FORMAT(c.createdAt, '%d.%m.%y %H:%i') as createdAt 
            from comment as c\s
            where c.post.id=:postId and c.state='ACTIVE'""")
    List<CommentRes> findAllByPostIdAndState(Long postId);
}
