package com.paulopsms.mapper;

import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User user;
    private UserEntity userEntity;
    private UserEntity userEntityException;

    @BeforeEach
    public void init() {
        user = User.builder().id(1L).name("John Doe").build();
        userEntity = new UserEntity(1L, "John Doe");
        userEntityException = new UserEntity(1L, "");
    }

    @Test
    public void givenAnUserModel_whenConvertToUserEntity_thenShouldReturnUserEntity() {
        UserEntity entity = userMapper.toEntity(user);

        assertEquals(this.user.getId(), entity.getId());
        assertEquals(this.user.getName(), entity.getName());
    }

    @Test
    public void givenAnUserEntity_whenConvertToUserModel_thenShouldReturnUserModel() {
        User userModel = userMapper.toModel(userEntity);

        assertEquals(this.userEntity.getId(), userModel.getId());
        assertEquals(this.userEntity.getName(), userModel.getName());
    }

    @Test
    public void givenAnEmptyUserModelName_whenConvertToUserEntity_thenShouldThrowAnException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userMapper.toModel(userEntityException);
        });

        String expectedMessage = "O nome do usuário é obrigatório.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }
}
