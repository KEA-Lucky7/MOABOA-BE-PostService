package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.domain.Hashtag;
import com.example.lucky7postservice.src.query.entity.post.QueryHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagQueryRepository extends JpaRepository<QueryHashtag, Long> {

}
