package com.khanh.livechat.repository;

import com.khanh.livechat.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

//    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
    @Query(value = "{participantIds: {$all: [?0, ?1]}}")
    Optional<ChatRoom> findByParticipantIds(Long senderId, Long recipientId);
}
