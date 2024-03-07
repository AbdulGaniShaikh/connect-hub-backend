package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.Message;
import com.shaikhabdulgani.ConnectHub.projection.InboxProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableMongoRepositories
public interface ChatRepo extends MongoRepository<Message,String> {
    Page<Message> findByChatId(String chatId, PageRequest date);

    @Aggregation(pipeline = {
            "{ $match: { $or: [{ senderId: ?0 }, { receiverId: ?0 }] } }",
            "{ $group: { _id: { $cond: { if: { $eq: ['$senderId', ?0] }, then: '$receiverId', else: '$senderId' } }, lastMessage: { $last: '$$ROOT' } } }",
            "{ $sort: { 'lastMessage.date': -1 } }",
            "{ $skip: ?1} ",
            "{ $limit: ?2 }",
            "{ $lookup: { from: 'users', let: { uId: '$_id' }, pipeline: [ { $match: { $expr: { $eq: ['$_id', { $toObjectId: '$$uId' }] } } } ], as: 'user' } }",
            "{ $unwind: '$user' }",
            "{ $project: { _id: 0, userId: '$_id', lastMessage: '$lastMessage.message', date: '$lastMessage.date', profileImageId: '$user.profileImageId', username: '$user.username', email: '$user.email', lastSeen: '$user.lastSeen',isPost:'$lastMessage.isPost',senderId:'$lastMessage.senderId' } }"
    })
    List<InboxProjection> findLastMessagesByUser(String userId,int skip,int limit);
}
