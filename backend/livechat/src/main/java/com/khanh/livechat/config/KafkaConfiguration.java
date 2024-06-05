package com.khanh.livechat.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.khanh.livechat.constant.AppConstant.LOGIN_TOPIC;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic loginTopic() {
        return TopicBuilder
                .name(LOGIN_TOPIC)
                .build();
    }
}
