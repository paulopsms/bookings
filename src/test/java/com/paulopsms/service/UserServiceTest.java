package com.paulopsms.service;

import com.paulopsms.domain.entity.User;
import com.paulopsms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private User user;
    private User user2;
    @BeforeEach
    public void createUser() {
        user = User.builder().id(1L).name("Paulo").build();
        user2 = User.builder().id(3L).name("Jorge").build();
    }

    @Test
    public void givenUserId_whenFindingAnUserAndIdIsInvalid_thenShouldReturnNull() {
        User user = userService.findUserById(999L);

        assertNull(user);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    public void givenUserId_whenFindingAnUserAndIdIsValid_thenShouldReturnTheUser() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        User user = userService.findUserById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Paulo", user.getName());
    }

    @Test
    public void givenANewUser_whenSavingUser_thenShouldBeSavedSuccessfully() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user2);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        User user = new User(3L, "Jorge");

        this.userRepository.save(user);
        User savedUser = userService.findUserById(3L);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        verify(userRepository, times(1)).save(Mockito.any());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
    }
}
