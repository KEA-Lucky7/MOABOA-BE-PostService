package com.example.lucky7postservice.src.post.domain.repository;

import com.example.lucky7postservice.src.post.domain.Hashtag;
import com.example.lucky7postservice.src.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByPostId(Long postId);
}
