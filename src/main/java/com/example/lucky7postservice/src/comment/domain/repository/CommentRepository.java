package com.example.lucky7postservice.src.comment.domain.repository;

import com.example.lucky7postservice.src.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
