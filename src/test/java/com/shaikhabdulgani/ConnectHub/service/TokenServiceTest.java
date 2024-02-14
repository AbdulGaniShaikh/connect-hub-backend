package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.model.Token;
import com.shaikhabdulgani.ConnectHub.repo.TokenRepo;
import com.shaikhabdulgani.ConnectHub.util.UniqueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = {TokenServiceTest.class})
@RunWith(SpringRunner.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepo tokenRepo;

    Token token;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        token =
                Token.builder()
                        .token(UniqueId.generateToken())
                        .userId("userdadasda")
                        .date(new Date(new Date().getTime()- Duration.ofHours(1).toMillis()))
                        .build();

    }

    @Test
    void test_getToken_ReturnToken() throws NotFoundException {

        Mockito.when(tokenRepo.findById(any())).thenReturn(Optional.of(token));
        assertThat(tokenService.getToken(token.getUserId())).isEqualTo(token);
    }

    @Test
    void test_getToken_ReturnNull() throws NotFoundException {

        Mockito.when(tokenRepo.findById(any())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(
                NotFoundException.class,
                ()->{
                    tokenService.getToken(token.getUserId());
                }
        );

        assertEquals(exception.getClass(), NotFoundException.class);
    }

    @Test
    void save() {

        Mockito.when(tokenRepo.save(any())).thenReturn(token);
        assertThat(tokenService.save("userdadasda")).isEqualTo(token);
    }
}