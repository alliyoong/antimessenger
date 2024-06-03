package com.khanh.livechat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatUser {
    @Id
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String bio;
    private LocalDateTime createdAt;
    private String imageUrl;
    private List<ChatUser> friendList;
}
