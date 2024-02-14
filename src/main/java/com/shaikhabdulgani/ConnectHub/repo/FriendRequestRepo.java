package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.FriendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface FriendRequestRepo extends MongoRepository<FriendRequest,String> {
    Optional<FriendRequest> findBySenderAndReceiver(String sender, String receiver);
    Page<FriendRequest> findByReceiver(String receiver, Pageable pageable);
    long countByReceiver(String receiver);
}
