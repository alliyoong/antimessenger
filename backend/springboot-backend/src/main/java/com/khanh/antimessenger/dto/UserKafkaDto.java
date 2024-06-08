package com.khanh.antimessenger.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserKafkaDto {
    private Long accountId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String bio;
    private LocalDateTime lastLoginDateDisplay;
    private Integer isEnabled;
    private Integer isNonLocked;
    private LocalDateTime createdAt;
    private String imageUrl;
}
