package com.khanh.antimessenger.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaService {
    @KafkaListener(topics = "login-topic", groupId = "live-chat-group")
    public void listenLogin(String message) {
        log.info("message nhan tu kafka la: {}", message);
    }
}
