package com.paulopsms.service;

import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import com.paulopsms.mapper.UserMapper;
import com.paulopsms.repository.UserRepository;
import com.paulopsms.validation.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private UserMapper userMapper;

    public User findUserById(Long userId) {
        UserEntity userEntity = this.userRepository.findById(userId).orElse(null);

        return this.userMapper.toModel(userEntity);
    }

    public User createUser(User user) {
        this.userValidationService.validateUser(user);

        UserEntity entity = userMapper.toEntity(user);

        this.userRepository.save(entity);

        return userMapper.toModel(entity);
    }

    public List<User> listUsers() {
        return this.userRepository.findAll().stream().map(this.userMapper::toModel).toList();
    }

    public User removeUser(Long userId) {
        return this.userRepository.findById(userId)
                .map(userEntity -> {
                    this.userRepository.delete(userEntity);
                    return this.userMapper.toModel(userEntity);
                })
                .orElse(null);
    }
}
