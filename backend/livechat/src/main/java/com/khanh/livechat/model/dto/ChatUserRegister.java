package com.khanh.livechat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatUserRegister {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
