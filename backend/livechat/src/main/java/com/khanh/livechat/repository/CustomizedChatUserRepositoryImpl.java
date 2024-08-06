package com.khanh.livechat.repository;

import com.khanh.livechat.model.ChatUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

@RequiredArgsConstructor
public class CustomizedChatUserRepositoryImpl implements CustomizedChatUserRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public List<ChatUser> findFriendList(String username) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("user_relation")
                .localField("userId")
                .foreignField("senderId")
                .as("friends");
        Aggregation aggregation = Aggregation
                .newAggregation(Aggregation.match(Criteria.where("userId").is("1")), lookupOperation);
        List<ChatUser> results = mongoTemplate.aggregate(aggregation, "chat_users", ChatUser.class).getMappedResults();
        return null;
    }
}
