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

    @Query(value = "{$and: [{'senderId': {$eq: ?0}}, {'receiverId': {$eq: ?1}}, {'status': {$eq: ?2}}]}")
    Optional<UserRelationship> findBySenderAndReceiverAndStatus(Long senderId, Long receiverId, String status);

    @Query(value = "{$and: [{'receiverId': {$eq: ?0}}, {'status': {$eq: ?1}}]}")
    List<UserRelationship> findWaitList(Long userId, String status);

    @Query(value = "{$and: [ {$or: [{'senderId': {$eq: ?0}}, {'receiverId': {$eq: ?0}}]}, {'status': ?1}]}")
    List<UserRelationship> findFriendList(Long userId, String status);

    @Query(value = "{$and: [{'senderId': {$eq: ?0}}, {'status': {$eq: ?1}}]}")
    List<UserRelationship> findPendingRequests(Long userId, String status);
}
