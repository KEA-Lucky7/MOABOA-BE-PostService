package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.comment.domain.Comment;
import com.example.lucky7postservice.src.query.entity.post.QueryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentQueryRepository extends JpaRepository<QueryComment, Long> {

}
