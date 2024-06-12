package com.khanh.livechat.service;

import com.khanh.livechat.model.ChatUser;
import com.khanh.livechat.model.dto.UserKafkaEvent;
import com.khanh.livechat.model.dto.UserKafkaDto;
import com.khanh.livechat.model.dto.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static com.khanh.livechat.constant.KafkaTopicName.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final ChatUserService chatUserService;

    private final KafkaTemplate<String, String> kafkaTemplate;

//    public String login(ChatUserLogin user) {
//        log.info("sending user to topic login :: {}", user.toString());
//        kafkaTemplate.send(KafkaTopicName.LOGIN_TOPIC, String.valueOf(user));
//        return "okay";
//    }

    @KafkaListener(topics = ADD_USER_TOPIC, groupId = "add-user-group")
    public void listenAddUser(@Payload UserKafkaEvent response) {
        var mapper = new DtoMapper<>(UserKafkaDto::new, ChatUser::new);
        ChatUser toAdd = mapper.toEntity(response.getUser());
        log.info("Add user nhan tu kafka la: {}", toAdd.toString());
        chatUserService.saveUser(toAdd);
    }

    @KafkaListener(topics = UPDATE_USER_TOPIC, groupId = "update-user-group")
    public void listenUpdateUser(@Payload UserKafkaEvent response) {
        var mapper = new DtoMapper<>(UserKafkaDto::new, ChatUser::new);
        ChatUser toUpdate = mapper.toEntity(response.getUser());
        log.info("Update user nhan tu kafka la: {}", toUpdate.toString());
        chatUserService.saveUser(toUpdate);
    }

    @KafkaListener(topics = DELETE_USER_TOPIC, groupId = "delete-user-group")
    public void listenDeleteUser(@Payload UserKafkaEvent response) {
//        var mapper = new DtoMapper<>(UserKafkaDto::new, ChatUser::new);
//        ChatUser toDelete = mapper.toEntity(response.getUser());
        var username = response.getUser().getUsername() ;
        log.info("Delete user nhan tu kafka la: {}", username);
        chatUserService.deleteUserByUsername(username);
    }
}
