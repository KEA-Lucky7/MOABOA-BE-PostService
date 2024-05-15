package com.example.lucky7postservice.src.comment.domain.repository;

import com.example.lucky7postservice.src.comment.domain.Reply;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findByIdAndState(Long replyId, State state);
    List<Reply> findAllByCommentPostIdAndState(Long postId, State state);

}
