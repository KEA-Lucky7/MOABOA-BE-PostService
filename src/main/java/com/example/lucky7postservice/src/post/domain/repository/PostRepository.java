package com.example.lucky7postservice.src.post.domain.repository;

import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndPostState(Long id, PostState state);
    List<Post> findAllByMemberIdAndPostState(Long memberId, PostState state);
}
