package com.example.lucky7postservice.src.like.domain.repository;

import com.example.lucky7postservice.src.like.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PostLikeRepository  extends JpaRepository<PostLike, Long> {
}
