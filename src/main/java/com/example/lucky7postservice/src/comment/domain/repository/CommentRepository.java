package com.example.lucky7postservice.src.comment.domain.repository;

import com.example.lucky7postservice.src.comment.domain.Comment;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndState(Long commentId, State state);
}
