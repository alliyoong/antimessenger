package com.khanh.antimessenger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNTVERIFICATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountVerificationId;

    @Column(name = "URL")
    private String url;
    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private MessAccount messAccount;
}
