package com.khanh.antimessenger.repository;

import com.khanh.antimessenger.model.AccountVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountVerificationRepository extends JpaRepository<AccountVerification, Long> {
    Optional<AccountVerification> findByUrl(String url);
}
