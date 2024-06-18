package com.khanh.livechat.model;

import com.khanh.livechat.constant.RelationshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("user_relationship")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRelationship {
    @Id
    private String id;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime requestTimestamp;
    private RelationshipStatus status;
}
