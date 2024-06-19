package com.khanh.livechat.model;

import com.khanh.livechat.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private String id;
    private String content;
    private MessageType type;
    private String chatRoomId;
    private String sender;
    private String sessionId;
    private LocalDateTime createdAt;
}
