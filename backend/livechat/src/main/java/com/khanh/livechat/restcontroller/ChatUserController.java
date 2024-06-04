package com.khanh.livechat.restcontroller;

import com.khanh.livechat.model.HttpResponse;
import com.khanh.livechat.model.dto.ChatUserLogin;
import com.khanh.livechat.model.dto.ChatUserRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:5000")
public class ChatUserController {

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        log.info("username login nhan duoc: {}", username);
        log.info("password login nhan duoc: {}", password);
        var user = ChatUserLogin.builder()
                .username(username)
                .password(password)
                .build();
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .data(Map.of("user", user))
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .message("You have login successfully")
                        .build()
        );
    }

    @PostMapping("register")
    public ResponseEntity<HttpResponse> register(
            @RequestParam("username") String username,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("password") String password,
            @RequestParam("email") String email
    ) {
        var user = ChatUserRegister.builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
        log.info("user register la: {}", user.toString());
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .data(Map.of("user", user))
                        .httpStatus(CREATED)
                        .httpStatusCode(CREATED.value())
                        .message("You have registered successfully")
                        .build()
        );
    }
}
