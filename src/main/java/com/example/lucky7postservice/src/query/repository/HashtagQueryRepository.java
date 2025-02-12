package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.query.entity.post.QueryHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagQueryRepository extends JpaRepository<QueryHashtag, Long> {
    @Query(value = "select h.content from hashtag as h where h.post.id=:postId")
    List<String> findAllByPostId(@Param("postId") Long postId);
}
