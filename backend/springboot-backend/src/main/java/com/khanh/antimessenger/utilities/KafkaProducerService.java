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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KafkaProducerService {
    private final KafkaTemplate<String, UserKafkaEvent> template;

    public void sendAddUserInfo(UserKafkaEvent event) {
        Message<UserKafkaEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicName.ADD_USER_TOPIC)
                .build();
        template.send(message);
    }

    public void sendUpdateUserInfo(UserKafkaEvent event) {
        Message<UserKafkaEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicName.UPDATE_USER_TOPIC)
                .build();
        template.send(message);
    }

    public void sendDeleteUserInfo(UserKafkaEvent event) {
        Message<UserKafkaEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopicName.DELETE_USER_TOPIC)
                .build();
        template.send(message);
    }
}
