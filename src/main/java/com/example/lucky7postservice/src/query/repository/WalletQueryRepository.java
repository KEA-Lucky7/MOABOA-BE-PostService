package com.example.lucky7postservice.src.query.repository;

import com.example.lucky7postservice.src.command.post.api.dto.WalletsRes;
import com.example.lucky7postservice.src.command.wallet.api.dto.ConsumedRes;
import com.example.lucky7postservice.src.query.entity.post.QueryWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletQueryRepository extends JpaRepository<QueryWallet, Long> {
    @Query(value = """
            select DATE_FORMAT(w.consumedDate, '%Y.%m.%d') as consumedDate,
            w.memo as memo, w.amount as amount, w.walletType as walletType
            from wallet as w
            where w.post.id=:postId and w.state='ACTIVE'""")
    List<WalletsRes> findAllByPostIdAndState(Long postId);

    @Query(value = """
            select DATE_FORMAT(w.consumedDate, '%Y.%m.%d') as consumedDate, sum(w.amount) as amount
            from wallet as w
            where w.member.id=:memberId and w.state='ACTIVE'
            and (w.consumedDate>=:startDate and w.consumedDate<:endDate)
            group by w.consumedDate
            order by w.consumedDate asc""")
    List<ConsumedRes> findAllByMemberIdAndStateAndConsumedDate(@Param("memberId") Long memberId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);

    @Query(value = """
            select sum(w.amount)
            from wallet as w
            where w.member.id=:memberId and w.state='ACTIVE'
            and (w.consumedDate>=:startDate and w.consumedDate<:endDate)""")
    Optional<Integer> findByMemberIdAndStateAndConsumedDate(@Param("memberId") Long memberId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);
}
