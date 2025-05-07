package com.paulopsms.mapper;

import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Property;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface PropertyMapper {

    Property toModel(PropertyEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "numberOfGuests", target = "numberOfGuests")
    @Mapping(source = "basePricePerNight", target = "basePricePerNight")
    PropertyEntity toEntity(Property dto);
}
