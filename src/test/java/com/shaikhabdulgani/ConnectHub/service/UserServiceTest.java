package com.shaikhabdulgani.ConnectHub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {TokenServiceTest.class})
@RunWith(SpringRunner.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private BasicUserService basicUserService;

    @BeforeEach
    void setUp() {



    }

    @Test
    void registerUser() {
    }
}