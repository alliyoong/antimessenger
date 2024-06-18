package com.khanh.livechat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document("chat_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"lastLoginDateDisplay", "createdAt"}, ignoreUnknown = true)
public class ChatUser {
    @Id
    private String id;
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginDateDisplay;
    private String imageUrl;
    private Integer isEnabled;
    private Integer isNonLocked;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChatUser)) {
            return false;
        }
        ChatUser other = (ChatUser) o;
        return other.userId == this.userId;
    }

    @Override
    public int hashCode() {
        return 31*(this.username.hashCode() + this.id.hashCode()) ;
    }

}
