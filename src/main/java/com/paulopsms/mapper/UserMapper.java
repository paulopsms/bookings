package com.paulopsms.mapper;

import com.paulopsms.domain.entity.UserEntity;
import com.paulopsms.domain.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface UserMapper {

    User toModel(UserEntity entity);

    UserEntity toEntity(User dto);
}
