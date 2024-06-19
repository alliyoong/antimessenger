package com.khanh.livechat.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.CommonErrorHandler;

import static com.khanh.livechat.constant.KafkaTopicName.ADD_USER_TOPIC;
import static com.khanh.livechat.constant.KafkaTopicName.UPDATE_USER_TOPIC;

@Configuration
public class KafkaConfiguration {

//    @Bean
//    NewTopic addUserTopic() {
//        return TopicBuilder
//                .name(ADD_USER_TOPIC)
//                .build();
//    }
//
//    @Bean
//    NewTopic updateUserTopic() {
//        return TopicBuilder
//                .name(UPDATE_USER_TOPIC)
//                .build();
//    }

    @Bean
    CommonErrorHandler commonErrorHandler() {
        return new KafkaErrorHandler();
    }
}
