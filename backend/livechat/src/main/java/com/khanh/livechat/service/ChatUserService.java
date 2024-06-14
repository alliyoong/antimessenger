package com.khanh.livechat.service;

import com.khanh.livechat.model.ChatUser;
import com.khanh.livechat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatUserService {
    private final ChatUserRepository userRepository;

    public ChatUser saveUser(ChatUser user) {
        ChatUser toUpdate = userRepository.findChatUserByUserId(user.getUserId()).orElse(null);
        if (toUpdate == null) {
            return userRepository.save(user);
        } else {
            toUpdate.setAddress(user.getAddress());
            toUpdate.setBio(user.getBio());
            toUpdate.setEmail(user.getEmail());
            toUpdate.setFirstName(user.getFirstName());
            toUpdate.setLastName(user.getLastName());
            toUpdate.setImageUrl(user.getImageUrl());
            toUpdate.setIsEnabled(user.getIsEnabled());
            toUpdate.setIsNonLocked(user.getIsNonLocked());
            toUpdate.setLastLoginDateDisplay(user.getLastLoginDateDisplay());
            toUpdate.setPhone(user.getPhone());
            toUpdate.setUsername(user.getUsername());
            return userRepository.save(toUpdate);
        }
    }

    public void deleteUserByUsername(String username) {
        userRepository.deleteChatUserByUsername(username);
    }

    public List<ChatUser> searchUserByUsername(String searchValue) {
        List<ChatUser> users = new ArrayList<>();
        if (searchValue != "") {
            users = userRepository.findChatUserByUsername(searchValue);
        }
        return users;
    }

}
