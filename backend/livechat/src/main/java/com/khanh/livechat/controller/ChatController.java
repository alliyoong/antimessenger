package com.khanh.livechat.controller;

import com.khanh.livechat.model.ChatMessage;
import com.khanh.livechat.model.ChatMessageBackup;
import com.khanh.livechat.model.ChatNotification;
import com.khanh.livechat.model.dto.HttpResponse;
import com.khanh.livechat.service.ChatMessageService;
import com.khanh.livechat.service.kafkachat.Sender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5000")
public class ChatController {
    private final Sender sender;
    private final SimpMessageSendingOperations messageTemplate;
    private final ChatMessageService chatMessageService;

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(
//            @Payload ChatMessage chatMessage,
//            SimpMessageHeaderAccessor headerAccessor
//    ) {
//        chatMessage.setSessionId(headerAccessor.getSessionId());
//        sender.send("messaging", chatMessage);
//        messageTemplate.convertAndSend("/topic/public",chatMessage);
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(
//            @Payload ChatMessage chatMessage,
//            SimpMessageHeaderAccessor headerAccessor
//    ) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        log.info("Chat message nhan duoc {}", chatMessage.toString());
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messageTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @GetMapping("/chat-message/{senderId}/{recipientId}")
    public ResponseEntity<HttpResponse> findChatMessages(@PathVariable Long senderId,
                                                              @PathVariable Long recipientId) {

        var messages = chatMessageService.findChatMessages(senderId, recipientId);
        return ResponseEntity.created(null).body(
                HttpResponse.builder()
                        .httpStatusCode(OK.value())
                        .httpStatus(OK)
                        .data(Map.of("chatHistory", messages))
                        .message("Successfully retrieve chat history")
                        .build()
        );
    }
}
