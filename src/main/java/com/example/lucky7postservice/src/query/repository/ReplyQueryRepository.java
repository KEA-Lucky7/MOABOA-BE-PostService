package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.ReplyRes;
import com.example.lucky7postservice.src.query.entity.post.QueryReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyQueryRepository extends JpaRepository<QueryReply, Long> {
    @Query(value = """
            select r.id as replyId, r.comment.id as commentId,
            r.member.id as memberId, r.member.nickname as nickname, r.member.profileImage as profileImg,
            r.content as content,
            DATE_FORMAT(r.createdAt, '%y.%m.%d %H:%i') as createdAt
            from reply as r
            where r.comment.id=:commentId and r.state='ACTIVE'""")
    List<ReplyRes> findAllByCommentIdAndState(@Param("commentId") Long commentId);
}
