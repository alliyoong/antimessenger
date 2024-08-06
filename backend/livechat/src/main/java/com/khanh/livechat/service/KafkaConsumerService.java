package com.khanh.livechat.service;

import com.khanh.livechat.model.ChatUser;
import com.khanh.livechat.model.dto.UserKafkaEvent;
import com.khanh.livechat.model.dto.UserKafkaDto;
import com.khanh.livechat.model.dto.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.khanh.livechat.constant.KafkaTopicName.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final ChatUserService chatUserService;

//    public String login(ChatUserLogin user) {
//        log.info("sending user to topic login :: {}", user.toString());
//        kafkaTemplate.send(KafkaTopicName.LOGIN_TOPIC, String.valueOf(user));
//        return "okay";
//    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000, multiplier = 2.0))
    @KafkaListener(topics = ADD_USER_TOPIC, groupId = "add-user-group")
    public void listenAddUser(@Payload UserKafkaEvent response) {
        var mapper = new DtoMapper<>(UserKafkaDto::new, ChatUser::new);
        ChatUser toAdd = mapper.toEntity(response.getUser());
        log.info("Add user nhan tu kafka la: {}", toAdd.toString());
        chatUserService.saveUser(toAdd);
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000, multiplier = 2.0))
    @KafkaListener(topics = UPDATE_USER_TOPIC, groupId = "update-user-group")
    public void listenUpdateUser(@Payload UserKafkaEvent response) {
        var mapper = new DtoMapper<>(UserKafkaDto::new, ChatUser::new);
        ChatUser toUpdate = mapper.toEntity(response.getUser());
        log.info("Update user nhan tu kafka la: {}", toUpdate.toString());
        chatUserService.saveUser(toUpdate);
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000, multiplier = 2.0))
    @KafkaListener(topics = DELETE_USER_TOPIC, groupId = "delete-user-group")
    public void listenDeleteUser(@Payload UserKafkaEvent response) {
//        var mapper = new DtoMapper<>(UserKafkaDto::new, ChatUser::new);
//        ChatUser toDelete = mapper.toEntity(response.getUser());
        var username = response.getUser().getUsername() ;
        log.info("Delete user nhan tu kafka la: {}", username);
        chatUserService.deleteUserByUsername(username);
    }

    @DltHandler
    public void dltHandler(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Dead messages received: {} from topic {}", message, topic);
    }
}
