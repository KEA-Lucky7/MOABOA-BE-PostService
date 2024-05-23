package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.query.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
}
