package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.model.Friend;
import com.shaikhabdulgani.ConnectHub.model.UnreadMessageCount;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.projection.InboxProjection;
import com.shaikhabdulgani.ConnectHub.projection.MessageCountProject;
import com.shaikhabdulgani.ConnectHub.repo.UnreadMessageCountRepo;
import com.shaikhabdulgani.ConnectHub.util.CustomPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnreadMessageCountService {

    private final UnreadMessageCountRepo repo;
    private final MongoTemplate mongoTemplate;

    public void save(Friend friend){
        repo.save(new UnreadMessageCount(friend.getFriendId(), friend.getUser1(), friend.getUser2(),0));
    }

    public long totalUnreadMessagesOfUser(String userId){
        Query countQuery = new Query(
                Criteria.where("receiverId").is(userId).and("count").gt(0)
        );
        return mongoTemplate.count(countQuery, "unreadMessageCount");
    }

    public long totalUnreadMessagesBetweenUsers(String senderId,String receiverId){
        Optional<UnreadMessageCount> messageCount = repo.findByReceiverIdAndSenderId(senderId,receiverId);
        return messageCount.orElseGet(UnreadMessageCount::new).getCount();
    }

    public boolean exists(String sender,String receiver){
        return repo.existsBySenderIdAndReceiverId(sender,receiver);
    }

    public void increaseCount(String senderId,String receiverId){
        Update update = new Update();
        update.inc("count");
        Query query = new Query(Criteria.where("senderId").is(senderId).and("receiverId").is(receiverId));
        mongoTemplate.updateFirst(query,update, UnreadMessageCount.class);
    }

    public void clearCount(String senderId,String receiverId){
        Update update = new Update();
        update.set("count",0);
        Query query = new Query(Criteria.where("senderId").is(senderId).and("receiverId").is(receiverId));
        mongoTemplate.updateFirst(query,update, UnreadMessageCount.class);
    }
}
