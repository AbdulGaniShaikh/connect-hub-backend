package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableMongoRepositories
public interface PostRepo extends MongoRepository<Post,String> {

    Page<Post> findByUserId(String userId,Pageable pageable);

    long countByPostId(String postId);
}
