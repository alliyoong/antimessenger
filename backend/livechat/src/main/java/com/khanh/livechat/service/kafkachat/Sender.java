package com.khanh.livechat.service.kafkachat;

import com.khanh.livechat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Sender {
    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public void send(String topic, ChatMessage message) {
        kafkaTemplate.send(topic, message);
    }
}
