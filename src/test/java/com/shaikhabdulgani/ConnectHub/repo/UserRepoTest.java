package com.shaikhabdulgani.ConnectHub.repo;

import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.projection.UserProjection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class UserRepoTest {


    @Autowired
    UserRepo userRepo;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp() {

        user1 = User.builder()
                .username("tehgan")
                .email("ehkas@gmail.com")
                .password("password")
                .isVerified(false)
                .build();

        user2 = User.builder()
                .username("tehgan2")
                .email("ehkaass@gmail.com")
                .password("password2")
                .isVerified(true)
                .build();

        user3 = User.builder()
                .username("hhh2")
                .email("a@gmail.com")
                .password("passwoasdrd2")
                .isVerified(false)
                .build();

        user1 = userRepo.save(user1);
        user2 = userRepo.save(user2);
        user3 = userRepo.save(user3);

    }

    @AfterEach
    void tearDown() {

//        userRepo.deleteById(user1.getUserId());
//        userRepo.deleteById(user2.getUserId());
//        userRepo.deleteById(user3.getUserId());
        userRepo.deleteAll();

    }

    @Test
    public void test_findByUsername_ReturnUser() {

        Optional<User> actual = userRepo.findByUsername(user1.getUsername());
        assertTrue(actual.isPresent());
        assertThat(actual.get().getUsername()).isEqualTo(user1.getUsername());
        assertThat(actual.get().getUserId()).isEqualTo(user1.getUserId());

    }

    @Test
    public void test_findByUsername_ReturnNull() {

        Optional<User> actual = userRepo.findByUsername("random");
        assertTrue(actual.isEmpty());

    }

    @Test
    public void test_findByEmail_ReturnUser() {

        Optional<User> actual = userRepo.findByEmail(user1.getEmail());
        assertTrue(actual.isPresent());
        assertThat(actual.get().getUsername()).isEqualTo(user1.getUsername());
        assertThat(actual.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(actual.get().getUserId()).isEqualTo(user1.getUserId());

    }

    @Test
    public void test_findByEmail_ReturnNull() {

        Optional<User> actual = userRepo.findByEmail("random");
        assertTrue(actual.isEmpty());

    }

    @Test
    public void test_updateLastSeenByUsername_ShouldUpdateDate() {

        Date lastSeen = new Date();
        userRepo.updateLastSeenByUsername(user1.getUsername(),lastSeen);
        Optional<User> result = userRepo.findById(user1.getUserId());
        assertThat(result.get().getLastSeen()).isEqualTo(lastSeen);

    }

    @Test
    public void test_findByUsernameRegex_Return2Users() {

        Pattern pattern = Pattern.compile(".*" + "teh" + ".*", Pattern.CASE_INSENSITIVE);

        Page<UserProjection> page = userRepo.findByUsernameRegex(pattern, Pageable.ofSize(10));
        page.forEach(user -> System.out.println(user.toString()));
        assertThat(page.getTotalElements()).isEqualTo(2);

    }

    @Test
    public void test_findByUsernameRegex_Return1Users() {

        Pattern pattern = Pattern.compile(".*" + "h2" + ".*", Pattern.CASE_INSENSITIVE);

        Page<UserProjection> page = userRepo.findByUsernameRegex(pattern, Pageable.ofSize(10));
        page.forEach(user -> System.out.println(user.toString()));
        assertThat(page.getTotalElements()).isEqualTo(1);

    }

    @Test
    public void test_setIsVerifyById_ShouldUpdateDate() {

        userRepo.setIsVerifyById(user3.getUserId(),true);
        Optional<User> result = userRepo.findById(user3.getUserId());
        assertTrue(result.get().isVerified());

    }
}