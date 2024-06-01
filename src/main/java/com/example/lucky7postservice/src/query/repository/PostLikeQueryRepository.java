package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.query.entity.post.QueryPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeQueryRepository extends JpaRepository<QueryPostLike, Long> {

}
