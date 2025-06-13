package com.example.users.unit.service;

import com.example.users.dto.ResponseDTO;
import com.example.users.dto.UserDTO;
import com.example.users.external.EmailClient;
import com.example.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTests {


    @Mock
    private EmailClient emailClient;

    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(emailClient);
    }

    @Test
    void createUserTest() {

        UserDTO userDTO = createUserDTO();

        ResponseDTO response = userService.createUser(userDTO);

        assertEquals("User created with Circuit Breaker", response.message());
        assertEquals(1, response.data().size());
        assertEquals(userDTO, response.data().get(0));
    }

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("edu");
        userDTO.setEmail("edu@teste.com.br");
        return userDTO;
    }
}
