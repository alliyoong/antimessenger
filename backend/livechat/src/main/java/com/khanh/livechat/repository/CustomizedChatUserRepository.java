package com.khanh.livechat.repository;

import com.khanh.livechat.model.ChatUser;

import java.util.List;

public interface CustomizedChatUserRepository {
    List<ChatUser> findFriendList(String username);
}
