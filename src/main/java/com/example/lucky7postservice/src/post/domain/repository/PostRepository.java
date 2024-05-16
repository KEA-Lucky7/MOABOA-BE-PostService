package com.example.lucky7postservice.src.post.domain.repository;

import com.example.lucky7postservice.src.post.domain.Post;
import com.example.lucky7postservice.src.post.domain.PostState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndPostState(Long id, PostState state);
    List<Post> findAllByMemberIdAndPostState(Long memberId, PostState state);

    @Query(value = "select p from Post p \n" +
            "where p.postState='ACTIVE' \n" +
            "order by (select count(l) from PostLike l where l.post.id = p.id) desc, p.id asc ")
    List<Post> findAllOrderByLikeCnt(Pageable pageable);

    @Query(value = "select p from Post as p \n" +
            "inner join PostLike as l on p.id=l.post.id \n" +
            "where p.postState='ACTIVE' \n" +
            "order by p.id")
    List<Post> findAllByLikeOrderById(Pageable pageable);
}
