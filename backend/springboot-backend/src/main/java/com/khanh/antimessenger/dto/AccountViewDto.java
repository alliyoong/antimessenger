package com.khanh.antimessenger.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.khanh.antimessenger.model.Role;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@JsonInclude(NON_DEFAULT)
public class AccountViewDto {
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
    private Integer isUsingMfa;
    private LocalDateTime createdAt;
    private String imageUrl;
    private Role role;
}
