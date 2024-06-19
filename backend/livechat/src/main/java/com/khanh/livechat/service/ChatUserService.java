package com.khanh.livechat.service;

import com.khanh.livechat.constant.RelationshipStatus;
import com.khanh.livechat.exception.ResourceAlreadyInUseException;
import com.khanh.livechat.model.ChatUser;
import com.khanh.livechat.model.UserRelationship;
import com.khanh.livechat.repository.ChatUserRepository;
import com.khanh.livechat.repository.UserRelationshipRepository;
import lombok.RequiredArgsConstructor;
import com.khanh.livechat.exception.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatUserService {
    private final ChatUserRepository userRepository;
    private final UserRelationshipRepository relationshipRepository;

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

    public String addFriend(Long userId, Long friendId) {
        // check exist
        ChatUser user = userRepository.findChatUserByUserId(userId).orElseThrow(
                () -> new ResourceNotFound("user", "userId", userId)
        );
        ChatUser friend = userRepository.findChatUserByUserId(friendId).orElseThrow(
                () -> new ResourceNotFound("user", "userId", friendId)
        );

        var check = checkRelation(userId, friendId);
        UserRelationship userRs;
        if (check == null) {
            var checkReverse = checkRelation(friendId, userId);
            if (checkReverse != null) {
                userRs = UserRelationship.builder()
                        .receiverId(friendId)
                        .senderId(userId)
                        .requestTimestamp(LocalDateTime.now())
                        .status(RelationshipStatus.FRIEND)
                        .build();
                relationshipRepository.delete(checkReverse);
                relationshipRepository.save(userRs);
            } else {
                userRs = UserRelationship.builder()
                        .receiverId(friendId)
                        .senderId(userId)
                        .requestTimestamp(LocalDateTime.now())
                        .status(RelationshipStatus.PENDING)
                        .build();
                relationshipRepository.save(userRs);
            }
        } else {
            throw new ResourceAlreadyInUseException("relationship", "id", check.getId());
        }
        return "Successfully send friend request";
    }

    public List<ChatUser> getFriendList(Long userId) {
        List<UserRelationship> relates = relationshipRepository.findFriendList(userId, RelationshipStatus.FRIEND.name());

        List<Long> friendIds = relates.stream()
                .map(item -> Objects.equals(item.getSenderId(), userId) ? item.getReceiverId() : item.getSenderId()).toList();

        List<ChatUser> friendList = friendIds.stream().map(id -> userRepository.findChatUserByUserId(id)
                .orElse(null)).collect(Collectors.toList());
        return friendList;
    }

    public List<ChatUser> getWaitList(Long userId) {
        List<ChatUser> waitList = relationshipRepository.findWaitList(userId, RelationshipStatus.PENDING.name()).stream()
                .map(item -> userRepository.findChatUserByUserId(item.getSenderId()).orElse(null)).collect(Collectors.toList());
        return waitList;
    }

    public List<ChatUser> getPendingRequests(Long userId) {
        List<ChatUser> pendingRequests = relationshipRepository.findPendingRequests(userId, RelationshipStatus.PENDING.name()).stream()
                .map(item -> userRepository.findChatUserByUserId(item.getReceiverId()).orElse(null)).collect(Collectors.toList());
        return pendingRequests;
    }

    private UserRelationship checkRelation(Long senderId, Long receiverId) {
//        var relate = relationshipRepository.findBySenderAndReceiver(sender.getUserId(), receiver.getUserId()).orElseThrow(
//                () -> new ResourceNotFound("sender", "senderId", sender.getUserId())
//        );
//        return relate.getStatus().equals(RelationshipStatus.FRIEND);
        var relation = relationshipRepository.findBySenderAndReceiver(senderId, receiverId).orElse(null);
        return relation;
    }

    public void removeRelation(Long userId, Long friendId) {
        UserRelationship relation = relationshipRepository.
                findBySenderAndReceiverAndStatus(userId, friendId, RelationshipStatus.PENDING.name())
                .orElseThrow(
                        () -> new ResourceNotFound("relation", "friendId", friendId)
                );
        relationshipRepository.delete(relation);
    }
}
