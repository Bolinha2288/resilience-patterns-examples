package com.example.users.controller;

import com.example.users.dto.ResponseDTO;
import com.example.users.dto.UserDTO;
import com.example.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/circuit-breaker")
    public ResponseEntity<ResponseDTO> createUserCircuitBreaker(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = this.userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/rate-limiter")
    public ResponseEntity<ResponseDTO> createUserRateLimit(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = this.userService.createUserRateLimiter(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/retry")
    public ResponseEntity<ResponseDTO> createUserRetry(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = this.userService.createUserRetry(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
