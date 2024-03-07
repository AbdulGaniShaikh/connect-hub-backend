package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableMongoRepositories
public interface RefreshTokenRepo extends MongoRepository<RefreshToken,String > {
}
