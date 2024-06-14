package com.khanh.livechat.repository;

import com.khanh.livechat.model.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends MongoRepository<ChatUser, String> {
    void deleteChatUserByUsername(String username);

    Optional<ChatUser> findChatUserByUserId(Long id);

    @Query(value = "{$or: [{'username': {$regex: ?0, $options: 'i'}}, " +
            "{'firstName': {$regex: ?0, $options: 'i'}}, " +
            "{'lastName': {$regex: ?0, $options: 'i'}}]}")
    List<ChatUser> findChatUserByUsername(String username);
}
