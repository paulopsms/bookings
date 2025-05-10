package com.paulopsms.service;

import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import com.paulopsms.exception.ValidationRuntimeException;
import com.paulopsms.mapper.UserMapper;
import com.paulopsms.repository.UserRepository;
import com.paulopsms.validation.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private UserValidationService userValidationService;

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
    public void givenInvalidUser_whenValidatingUser_thenShouldThrowValidationRuntimeException() {
        User invalidUser = User.builder().id(1L).build();
        Mockito.doThrow(new ValidationRuntimeException("O nome do usuário é obrigatório."))
                .when(userValidationService).validateUser(Mockito.any(User.class));

        ValidationRuntimeException exception = assertThrows(ValidationRuntimeException.class, () -> {
            userService.createUser(invalidUser);
        });

        assertEquals("O nome do usuário é obrigatório.", exception.getMessage());
        verify(userValidationService, times(1)).validateUser(Mockito.any(User.class));
        verify(userRepository, times(0)).save(Mockito.any(UserEntity.class));
    }


    @Test
    public void givenValidUser_whenSavingUser_thenUserShouldBeSavedSuccessfully() {
        Mockito.doNothing().when(userValidationService).validateUser(Mockito.any(User.class));
        Mockito.when(userMapper.toEntity(Mockito.any(User.class))).thenReturn(userEntity);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        Mockito.when(userMapper.toModel(Mockito.any(UserEntity.class))).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        assertEquals("Paulo", savedUser.getName());
        verify(userValidationService, times(1)).validateUser(Mockito.any(User.class));
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    public void givenUserId_whenDeletingUser_thenShouldDeleteSuccessfully() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userEntity));
        Mockito.doNothing().when(userRepository).delete(Mockito.any(UserEntity.class));
        Mockito.when(userMapper.toModel(Mockito.any(UserEntity.class))).thenReturn(user);

        User removedUser = userService.removeUser(1L);

        assertEquals(1L, removedUser.getId());
        assertEquals("Paulo", removedUser.getName());
        verify(userRepository, times(1)).findById(Mockito.eq(1L));
        verify(userRepository, times(1)).delete(Mockito.any(UserEntity.class));
    }

    @Test
    public void givenUsersExist_whenListingUsers_thenAllUsersShouldBeListed() {
        List<UserEntity> userEntities = List.of(
                UserEntity.builder().id(1L).name("Paulo").build(),
                UserEntity.builder().id(2L).name("Maria").build()
        );

        List<User> users = List.of(
                User.builder().id(1L).name("Paulo").build(),
                User.builder().id(2L).name("Maria").build()
        );

        Mockito.when(userRepository.findAll()).thenReturn(userEntities);
        Mockito.when(userMapper.toModel(userEntities.get(0))).thenReturn(users.get(0));
        Mockito.when(userMapper.toModel(userEntities.get(1))).thenReturn(users.get(1));

        List<User> returnedUsers = userService.listUsers();

        assertNotNull(returnedUsers);
        assertEquals(2, returnedUsers.size());
        assertEquals("Paulo", returnedUsers.get(0).getName());
        assertEquals("Maria", returnedUsers.get(1).getName());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).toModel(Mockito.any(UserEntity.class));
    }
}
