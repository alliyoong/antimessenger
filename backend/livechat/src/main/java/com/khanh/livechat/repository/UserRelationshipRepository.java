package com.khanh.livechat.repository;

import com.khanh.livechat.model.UserRelationship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRelationshipRepository extends MongoRepository<UserRelationship, String> {

//    @Query(value = "{$and: [{'senderId': {$eq: ?0}}, {'receiverId': {$eq: ?1}}]}", fields = "{'id': 0, 'status': 1}")
//    Optional<UserRelationship> findBySenderAndReceiver(Long senderId, Long receiverId);

    @Query(value = "{$and: [{'senderId': {$eq: ?0}}, {'receiverId': {$eq: ?1}}]}")
    Optional<UserRelationship> findBySenderAndReceiver(Long senderId, Long receiverId);

    @Query(value = "{$and: [{'receiverId': {$eq: ?0}}, {'status': {$eq: 'PENDING'}}]}", fields = "{'id': 0, 'senderId': 1}")
    List<Long> findWaitList(Long userId);

    @Query(value = "{$and: [ {$or: [{'senderId': {$eq: ?0}}, {'receiverId': {$eq: ?0}}]}, {'status': 'FRIEND'}]}")
    List<UserRelationship> findFriendList(Long userId);

    @Query(value = "{$and: [{'senderId': {$eq: ?0}}, {'status': {$eq: 'PENDING'}}]}", fields = "{'id': 0, 'receiverId': 1}")
    List<Long> findPendingRequests(Long userId);
}
