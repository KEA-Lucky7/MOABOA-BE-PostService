package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.comment.domain.Reply;
import com.example.lucky7postservice.src.query.entity.post.QueryReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyQueryRepository extends JpaRepository<QueryReply, Long> {

}
