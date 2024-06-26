package com.khanh.livechat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String chatRoomId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private LocalDateTime timestamp;
}
