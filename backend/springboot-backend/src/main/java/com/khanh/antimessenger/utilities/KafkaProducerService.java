package com.khanh.antimessenger.utilities;

import com.khanh.antimessenger.constant.KafkaTopicName;
import com.khanh.antimessenger.dto.UserKafkaEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.khanh.antimessenger.constant.KafkaTopicName.ADD_USER_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KafkaProducerService {
    private final KafkaTemplate<String, UserKafkaEvent> template;

    public void sendUserInfo(UserKafkaEvent event, String topic) {
        Message<UserKafkaEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        template.send(message);
    }
}
