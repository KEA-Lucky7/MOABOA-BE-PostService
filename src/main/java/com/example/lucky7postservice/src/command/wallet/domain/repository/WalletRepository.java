package com.example.lucky7postservice.src.command.wallet.domain.repository;

import com.example.lucky7postservice.src.command.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllByPostId(Long postId);
}
