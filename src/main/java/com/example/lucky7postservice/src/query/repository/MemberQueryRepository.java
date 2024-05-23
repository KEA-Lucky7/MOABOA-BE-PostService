package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.query.member.Member;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndState(Long memberId, State state);
}
