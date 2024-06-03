package com.khanh.antimessenger.repository;

import com.khanh.antimessenger.model.MessAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessAccountRepository extends JpaRepository<MessAccount,Long> {
    Optional<MessAccount> findMessAccountByUsername(String username);
    Optional<MessAccount> findMessAccountByEmail(String username);
    Optional<MessAccount> findMessAccountByAccountId(Long id);
    Integer deleteMessAccountByAccountId(Long id);
}
