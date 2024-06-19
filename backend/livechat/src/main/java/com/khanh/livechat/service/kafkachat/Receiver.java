package com.khanh.livechat.service.kafkachat;

import com.khanh.livechat.constant.KafkaTopicName;
import com.khanh.livechat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import static com.khanh.livechat.constant.KafkaTopicName.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class Receiver {
    private final SimpMessageSendingOperations messagingTemplate;
    private final SimpUserRegistry userRegistry;

    @KafkaListener(topics = CHAT_TOPIC, groupId = "chat")
    public void consume(ChatMessage chatMessage) {
        log.info("Received message from Kafka: {}", chatMessage.toString());
        for (SimpUser user : userRegistry.getUsers()) {
            for (SimpSession session : user.getSessions()) {
                if (!session.getId().equals(chatMessage.getSessionId())) {
                    messagingTemplate.convertAndSendToUser(session.getId(), "/topic/public", chatMessage);
                }
            }
        }
    }
}
