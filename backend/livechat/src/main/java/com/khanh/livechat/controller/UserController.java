package com.khanh.livechat.controller;

import com.khanh.livechat.model.ChatUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public ChatUser addUser(
            @Payload ChatUser user
    ) {
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public ChatUser disconnectUser(
            @Payload ChatUser user
    ) {
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<ChatUser>> findConnectedUsers() {
        return ResponseEntity.ok(null);
    }
}
