package com.example.users.service;

import com.example.users.domain.model.User;
import com.example.users.domain.repository.UserRepository;
import com.example.users.dto.ResponseDTO;
import com.example.users.dto.UserDTO;
import com.example.users.external.EmailClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final EmailClient emailClient;

    public UserService(ModelMapper modelMapper, UserRepository userRepository, EmailClient emailClient) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.emailClient = emailClient;
    }

    public ResponseDTO createUser(UserDTO userDTO) {
        log.info("Starting user create: {}", userDTO);

        try {
            User user = modelMapper.map(userDTO, User.class);
            userRepository.save(user);
        } catch (DataAccessException e) {
            log.error("Error saving user to the database: {}", e.getMessage());
            throw new RuntimeException("Failed to save user to the database");
        }

        emailClient.sendWelcomeEmail(userDTO);
        log.info("Successfully user create: {}", userDTO);
        return new ResponseDTO("User created", List.of(userDTO));
    }
}