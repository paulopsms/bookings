package com.paulopsms.mapper;

import com.paulopsms.domain.entity.DateRangeEntity;
import com.paulopsms.domain.model.DateRange;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface DateRangeMapper {

    DateRange toModel(DateRangeEntity entity);

    DateRangeEntity toEntity(DateRange dto);
}
