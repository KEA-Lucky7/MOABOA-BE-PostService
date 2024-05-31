package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.WalletsRes;
import com.example.lucky7postservice.src.query.entity.post.QueryWallet;
import com.example.lucky7postservice.utils.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletQueryRepository extends JpaRepository<QueryWallet, Long> {
    @Query(value = """
            select DATE_FORMAT(w.consumedDate, '%y.%m.%d') as consumedDate,
            w.memo as memo, w.amount as amount, w.walletType as walletType
            from wallet as w
            where w.post.id=:postId and w.state='ACTIVE'""")
    List<WalletsRes> findAllByPostIdAndState(Long postId);
}
