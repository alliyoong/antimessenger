package com.khanh.antimessenger.dto;

import com.khanh.antimessenger.constant.KafkaEventType;
import com.khanh.antimessenger.model.MessAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserKafkaEvent {
    private KafkaEventType type;
    private UserKafkaDto user;
}
