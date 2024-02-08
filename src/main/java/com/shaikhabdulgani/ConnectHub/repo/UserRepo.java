package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface UserRepo extends MongoRepository<User,String> {

    Optional<User> findByUsername(String s);
    Optional<User> findByEmail(String s);

    @Query("{'username' : ?0}")
    @Update("{'$set': {'lastSeen': ?1}}")
    void updateLastSeenByUsername(String username, Date lastSeen);

    @Query("{'_id' : ?0}")
    @Update("{'$set': {'isVerified': ?1}}")
    void setIsVerifyById(String userId,boolean isVerified);

    @Query(fields = "{ 'password' : 0 } ")
    Page<User> findByUsernameRegex(String username, Pageable pageable);
}
