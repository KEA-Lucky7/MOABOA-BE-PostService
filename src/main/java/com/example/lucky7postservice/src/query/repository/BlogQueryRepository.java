package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.query.entity.blog.QueryBlog;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogQueryRepository extends JpaRepository<QueryBlog, Long> {
    Optional<QueryBlog> findByMemberIdAndState(Long memberId, State state);
    Optional<QueryBlog> findByIdAndState(Long blogId, State state);
}
