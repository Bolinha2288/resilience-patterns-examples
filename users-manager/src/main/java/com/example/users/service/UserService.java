package com.example.users.service;

import com.example.users.dto.ResponseDTO;
import com.example.users.dto.UserDTO;
import com.example.users.external.EmailClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final EmailClient emailClient;

    public UserService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public ResponseDTO createUser(UserDTO userDTO) {
        log.info("Starting user create with Circuit Breaker: {}", userDTO);
        emailClient.sendWelcomeEmailWithCircuitBreaker(userDTO);
        log.info("Successfully user create: {}", userDTO);
        return new ResponseDTO("User created with Circuit Breaker", List.of(userDTO));
    }

    public ResponseDTO createUserRateLimiter(UserDTO userDTO) {
        log.info("Starting user create with Rate Limit: {}", userDTO);
        emailClient.sendWelcomeEmailWithRateLimit(userDTO);
        log.info("Successfully user create with Rate Limit: {}", userDTO);
        return new ResponseDTO("User created", List.of(userDTO));
    }

    public ResponseDTO createUserRetry(UserDTO userDTO) {
        log.info("Starting user create with Retry: {}", userDTO);
        emailClient.sendWelcomeEmailWithRetry(userDTO);
        log.info("Successfully user create with Retry: {}", userDTO);
        return new ResponseDTO("User created", List.of(userDTO));
    }


}