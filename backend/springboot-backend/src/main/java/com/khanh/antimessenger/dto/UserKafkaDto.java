package com.khanh.antimessenger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserKafkaDto {
    private Long userId;
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
