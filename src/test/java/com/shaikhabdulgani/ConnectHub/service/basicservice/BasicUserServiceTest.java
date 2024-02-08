package com.shaikhabdulgani.ConnectHub.service.basicservice;

import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {BasicUserServiceTest.class})
@RunWith(SpringRunner.class)
class BasicUserServiceTest {

    @InjectMocks
    private UserRepo userRepo;

    User user;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByEmail() {
    }

    @Test
    void getById() {
    }

    @Test
    void getUser() {
    }

    @Test
    void checkIfUserAlreadyExists() {
    }

    @Test
    void updateWithPassword() {
    }
}