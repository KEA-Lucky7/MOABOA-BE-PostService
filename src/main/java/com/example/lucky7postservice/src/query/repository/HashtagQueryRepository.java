package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagQueryRepository extends JpaRepository<Hashtag, Long> {

}
