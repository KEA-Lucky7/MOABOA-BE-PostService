package com.example.lucky7postservice.src.post.domain.repository;

import com.example.lucky7postservice.src.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post, Long> {
}
