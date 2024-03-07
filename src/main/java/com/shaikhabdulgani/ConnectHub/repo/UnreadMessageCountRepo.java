package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.UnreadMessageCount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
@Repository
public interface UnreadMessageCountRepo extends MongoRepository<UnreadMessageCount,String> {

    Optional<UnreadMessageCount> findByReceiverIdAndSenderId(String receiver, String sender);

}
