package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface FriendRepo extends MongoRepository<Friend,String> {

    Optional<Friend> findByUser1AndUser2(String user1,String user2);
    Page<Friend> findByUser1(String userId, Pageable pageable);

}
