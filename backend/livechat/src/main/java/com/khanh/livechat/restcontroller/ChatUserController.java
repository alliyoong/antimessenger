package com.khanh.livechat.restcontroller;

import com.khanh.livechat.model.ChatUser;
import com.khanh.livechat.model.dto.HttpResponse;
import com.khanh.livechat.model.dto.ChatUserLogin;
import com.khanh.livechat.model.dto.ChatUserRegister;
import com.khanh.livechat.service.ChatUserService;
import com.khanh.livechat.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5000")
@RequestMapping("/user")
public class ChatUserController {
    private final ChatUserService userService;

    @GetMapping(value = {"/search/{searchValue}", "/search/"})
    public ResponseEntity<HttpResponse> searchUser(
            @PathVariable(required = false) Optional<String> searchValue
    ) {
        List<ChatUser> users = userService.searchUserByUsername(searchValue.orElse(""));
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .data(Map.of("users", users))
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .message("Successfully retrieved list of users")
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
