package com.example.lucky7postservice.src.command.post.domain.repository;

import com.example.lucky7postservice.src.command.post.domain.Post;
import com.example.lucky7postservice.src.command.post.domain.PostState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndPostState(Long id, PostState state);
}
