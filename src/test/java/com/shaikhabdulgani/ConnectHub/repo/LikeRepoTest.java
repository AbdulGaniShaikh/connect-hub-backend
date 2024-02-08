package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.Like;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class LikeRepoTest {

    @Autowired
    private LikeRepo repo;

    Like like;

    @BeforeEach
    void setUp() {
        like = Like.builder()
                .postId("post1")
                .userId("user1")
                .build();
        like = repo.save(like);
    }

    @AfterEach
    void tearDown() {

        repo.deleteById(like.getLikeId());

    }

    @Test
    void findByUserIdAndPostId() {

        Optional<Like> actual = repo.findByUserIdAndPostId(like.getUserId(), like.getPostId());
        assertTrue(actual.isPresent());
        assertThat(actual.get()).isEqualTo(like);

    }
}