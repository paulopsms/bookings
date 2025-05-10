package com.paulopsms.mapper;

import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    public void init() {
        user = User.builder().id(1L).name("John Doe").build();
        userEntity = new UserEntity(1L, "John Doe");
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
}
