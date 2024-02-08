package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.Bookmark;
import com.shaikhabdulgani.ConnectHub.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface BookmarkRepo extends MongoRepository<Bookmark,String> {
    Optional<Bookmark> findByUserIdAndPostId(String userId, String postId);

    @Query
    Page<Bookmark> findByUserId(String userId, Pageable pageable);
}
