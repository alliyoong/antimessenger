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

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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

    @GetMapping ("/add-friend/{userId}/{friendId}")
    public ResponseEntity<HttpResponse> addFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        String message = userService.addFriend(userId, friendId);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .message(message)
                        .build()
        );
    }

    @GetMapping("/friend-list/{userId}")
    public ResponseEntity<HttpResponse> getFriendList(@PathVariable("userId") Long userId) {
        var data = userService.getFriendList(userId);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("friendList", data))
                        .message("Successfully retrieve data")
                        .build()
        );
    }

    @GetMapping("/wait-list/{userId}")
    public ResponseEntity<HttpResponse> getWaitList(@PathVariable("userId") Long userId) {
        var data = userService.getWaitList(userId);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("waitList", data))
                        .message("Successfully retrieve data")
                        .build()
        );
    }

    @GetMapping("/pending-requests/{userId}")
    public ResponseEntity<HttpResponse> getPendingRequests(@PathVariable("userId") Long userId) {
        var data = userService.getPendingRequests(userId);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("pendingRequests", data))
                        .message("Successfully retrieve data")
                        .build()
        );
    }

    @GetMapping("/cancel-requests/{userId}/{friendId}")
    public ResponseEntity<HttpResponse> cancelRequest(
            @PathVariable("userId") Long userId,
            @PathVariable("friendId") Long friendId
    ) {
        userService.removeRelation(userId, friendId);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(NO_CONTENT.value())
                        .httpStatus(NO_CONTENT)
                        .message("Successfully cancel request")
                        .build()
        );
    }
}
