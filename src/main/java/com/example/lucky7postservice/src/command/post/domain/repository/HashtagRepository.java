package com.example.lucky7postservice.src.command.post.domain.repository;

import com.example.lucky7postservice.src.command.post.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByPostId(Long postId);
}
