package com.khanh.livechat.service;

import com.khanh.livechat.model.dto.UserKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static com.khanh.livechat.constant.KafkaTopicName.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public String login(ChatUserLogin user) {
//        log.info("sending user to topic login :: {}", user.toString());
//        kafkaTemplate.send(LOGIN_TOPIC, String.valueOf(user));
//        return "okay";
//    }

    @KafkaListener(topics = ADD_USER_TOPIC, groupId = "add-user-group")
    public UserKafkaEvent listenAddUser(@Payload UserKafkaEvent response) {
        log.info("message nhan tu kafka la: {}", response.toString());
        return response;
    }

    @KafkaListener(topics = UPDATE_USER_TOPIC, groupId = "update-user-group")
    public UserKafkaEvent listenUpdateUser(@Payload UserKafkaEvent response) {
        log.info("message nhan tu kafka la: {}", response.toString());
        return response;
    }

    @KafkaListener(topics = DELETE_USER_TOPIC, groupId = "delete-user-group")
    public UserKafkaEvent listenDeleteUser(@Payload UserKafkaEvent response) {
        log.info("message nhan tu kafka la: {}", response.toString());
        return response;
    }
}
