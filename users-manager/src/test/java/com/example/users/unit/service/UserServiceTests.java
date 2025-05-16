package com.example.users.unit.service;

import com.example.users.domain.model.User;
import com.example.users.domain.repository.UserRepository;
import com.example.users.dto.ResponseDTO;
import com.example.users.dto.UserDTO;
import com.example.users.external.EmailClient;
import com.example.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTests {


    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailClient emailClient;

    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(modelMapper, userRepository, emailClient);
    }

    @Test
    void createUserTest() {

        UserDTO userDTO = createUserDTO();

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        ResponseDTO response = userService.createUser(userDTO);

        assertEquals("User created", response.message());
        assertEquals(1, response.data().size());
        assertEquals(userDTO, response.data().get(0));
    }

    @Test
    void shouldThrowExceptionWhenDatabaseFails() {

        UserDTO userDTO = createUserDTO();
        User user = new User();

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        doThrow(new DataAccessException("Database error") {}).when(userRepository).save(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(userDTO));
        assertEquals("Failed to save user to the database", exception.getMessage());

        verify(userRepository, times(1)).save(user);
    }

    

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("edu");
        userDTO.setEmail("edu@teste.com.br");
        return userDTO;
    }
}
