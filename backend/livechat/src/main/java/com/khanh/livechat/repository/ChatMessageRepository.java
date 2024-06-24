package com.khanh.livechat.repository;

import com.khanh.livechat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
//    List<ChatMessageBackup> findChatMessageByChatRoomId(String chatRoomId);
    List<ChatMessage> findByChatRoomId(String chatRoomId);

//    List<ChatMessage> findChatMessageByChatRoomId(String chatRoomId);
}
