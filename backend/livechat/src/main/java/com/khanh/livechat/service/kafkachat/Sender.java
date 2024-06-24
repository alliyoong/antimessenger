package com.khanh.livechat.service.kafkachat;

import com.khanh.livechat.model.ChatMessageBackup;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Sender {
    private final KafkaTemplate<String, ChatMessageBackup> kafkaTemplate;

    public void send(String topic, ChatMessageBackup message) {
        kafkaTemplate.send(topic, message);
    }
}
