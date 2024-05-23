package com.example.lucky7postservice.src.command.comment.domain.repository;

import com.example.lucky7postservice.src.command.comment.domain.Comment;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndState(Long commentId, State state);
    List<Comment> findAllByPostIdAndState(Long postId, State state);
}
