package com.khanh.livechat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("chat_rooms")
public class ChatRoom {
    @Id
    private String id;
    private String chatRoomId;
//    private String senderId;
//    private String recipientId;
    private String adminId;
    private List<Long> participantIds;
    private LocalDateTime createdAt;
}
