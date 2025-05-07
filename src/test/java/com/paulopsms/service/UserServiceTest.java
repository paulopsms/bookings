package com.paulopsms.service;

import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import com.paulopsms.mapper.UserMapper;
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
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    public void createUser() {
        userEntity = UserEntity.builder().id(1L).name("Paulo").build();
        user = User.builder().id(1L).name("Paulo").build();
    }

    @Test
    public void givenUserId_whenFindingAnUserAndIdIsInvalid_thenShouldReturnNull() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(userMapper.toModel(Mockito.any(UserEntity.class))).thenReturn(null);

        User user = userService.findUserById(999L);

        assertNull(user);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(userMapper, times(1)).toModel(Mockito.isNull());
    }

    @Test
    public void givenUserId_whenFindingAnUserAndIdIsValid_thenShouldReturnTheUser() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapper.toModel(Mockito.any(UserEntity.class))).thenReturn(user);

        User user = userService.findUserById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Paulo", user.getName());
        verify(userMapper, times(1)).toModel(Mockito.any(UserEntity.class));
    }

    @Test
    public void givenANewUser_whenSavingUser_thenShouldBeSavedSuccessfully() {
        UserEntity user = new UserEntity(1L, "Paulo");
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapper.toModel(Mockito.any(UserEntity.class))).thenReturn(this.user);

        this.userRepository.save(user);
        User savedUser = userService.findUserById(1L);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        verify(userRepository, times(1)).save(Mockito.any());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(userMapper, times(1)).toModel(Mockito.any(UserEntity.class));
    }
}
