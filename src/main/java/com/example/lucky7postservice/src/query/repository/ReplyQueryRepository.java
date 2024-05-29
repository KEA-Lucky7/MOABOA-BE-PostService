package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.ReplyRes;
import com.example.lucky7postservice.src.query.entity.post.QueryReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyQueryRepository extends JpaRepository<QueryReply, Long> {
    @Query(value = """
            select r.id as replyId, r.comment.id as commentId,\s
            r.member.id as memberId, r.member.nickname as nickname, r.member.profileImage as profileImg,\s
            r.content as content,\s
            DATE_FORMAT(r.createdAt, '%d.%m.%y %H:%i') as createdAt\s
            from reply as r\s
            where r.comment.id=:commentId and r.state='ACTIVE'""")
    List<ReplyRes> findAllByCommentIdAndState(Long commentId);
}
