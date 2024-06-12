package com.khanh.livechat.repository;

import com.khanh.livechat.model.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatUserRepository extends MongoRepository<ChatUser, String> {
    void deleteChatUserByUsername(String username);

    Optional<ChatUser> findChatUserByUserId(Long id);
}
