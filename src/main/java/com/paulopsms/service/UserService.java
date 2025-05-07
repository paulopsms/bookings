package com.paulopsms.service;

import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import com.paulopsms.mapper.UserMapper;
import com.paulopsms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public User findUserById(Long userId) {
        UserEntity userEntity = this.userRepository.findById(userId).orElse(null);

        return this.userMapper.toModel(userEntity);
    }

    public User createUser(User user) {
        UserEntity entity = userMapper.toEntity(user);
        User model = userMapper.toModel(entity);

        this.userRepository.save(entity);

        model.setId(entity.getId());

        return model;
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
