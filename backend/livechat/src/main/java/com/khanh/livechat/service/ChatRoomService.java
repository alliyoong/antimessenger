package com.khanh.livechat.service;

import com.khanh.livechat.model.ChatRoom;
import com.khanh.livechat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
            Long senderId,
            Long recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findByParticipantIds(senderId, recipientId)
                .map(ChatRoom::getChatRoomId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatRoomId = createChatId(senderId, recipientId);
                        return Optional.of(chatRoomId);
                    }
                    return  Optional.empty();
                });
    }

    private String createChatId(Long senderId, Long recipientId) {
        var chatRoomId = String.format("%s_%s", senderId, recipientId);

        ChatRoom toCreate = ChatRoom
                .builder()
                .chatRoomId(chatRoomId)
                .participantIds(List.of(senderId, recipientId))
                .createdAt(LocalDateTime.now())
                .build();
        chatRoomRepository.save(toCreate);

        return chatRoomId;
    }
}
