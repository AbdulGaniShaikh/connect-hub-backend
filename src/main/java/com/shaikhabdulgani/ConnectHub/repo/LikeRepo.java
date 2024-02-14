package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface LikeRepo extends MongoRepository<Like,String > {

    Optional<Like> findByUserIdAndPostId(String userId,String postId);

    boolean existsByUserIdAndPostId(String userId, String postId);
}
