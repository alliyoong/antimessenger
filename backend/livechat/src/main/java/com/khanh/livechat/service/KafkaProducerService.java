package com.khanh.livechat.service;

import com.khanh.livechat.model.dto.ChatUserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.khanh.livechat.constant.AppConstant.LOGIN_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public String login(ChatUserLogin user){
        log.info("sending user to topic login :: {}", user.toString());
        kafkaTemplate.send(LOGIN_TOPIC, String.valueOf(user));
        return "okay";
    }
}
