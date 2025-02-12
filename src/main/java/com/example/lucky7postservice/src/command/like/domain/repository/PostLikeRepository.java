package com.example.lucky7postservice.src.command.like.domain.repository;

import com.example.lucky7postservice.src.command.like.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository  extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndMemberId(Long postId, Long memberId);
    List<PostLike> findAllByPostId(Long postId);
}
