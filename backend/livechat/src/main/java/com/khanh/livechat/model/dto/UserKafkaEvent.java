package com.khanh.livechat.model.dto;

import com.khanh.livechat.constant.KafkaEventType;
import lombok.Data;

@Data
public class UserKafkaEvent {
    private KafkaEventType type;
    private UserKafkaDto user;
}
