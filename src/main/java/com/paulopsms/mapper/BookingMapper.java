package com.paulopsms.mapper;

import com.paulopsms.domain.entity.BookingEntity;
import com.paulopsms.domain.model.Booking;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface BookingMapper {

    Booking toModel(BookingEntity entity);

    BookingEntity toEntity(Booking model);
}
