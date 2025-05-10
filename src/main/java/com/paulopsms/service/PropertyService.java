package com.paulopsms.service;

import com.paulopsms.domain.entity.BookingEntity;
import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.Property;
import com.paulopsms.mapper.BookingMapper;
import com.paulopsms.mapper.PropertyMapper;
import com.paulopsms.repository.BookingRepository;
import com.paulopsms.repository.PropertyRepository;
import com.paulopsms.validation.PropertyValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private PropertyValidationService propertyValidationService;

    public Property findPropertyById(Long propertyId) {
        PropertyEntity propertyEntity = this.propertyRepository.findById(propertyId).orElse(null);

        Property model = propertyMapper.toModel(propertyEntity);

        if (nonNull(propertyEntity)) {
            List<BookingEntity> bookingsByPropertyId = this.bookingRepository.findAllByPropertyId(model.getId());

            List<Booking> list = bookingsByPropertyId.stream().map(bookingMapper::toModel).toList();

            list.forEach(model::addBooking);
        }

        log.info("Property found: {}", model);

        return model;
    }

    public Property saveProperty(Property property) {
        this.propertyValidationService.validateProperty(property);

        log.info("Saving Property: {}", property);

        PropertyEntity entity = this.propertyMapper.toEntity(property);

        this.propertyRepository.save(entity);

        return propertyMapper.toModel(entity);
    }

    public Property removeProperty(Long propertyId) {
        return this.propertyRepository.findById(propertyId)
                .map(userEntity -> {
                    this.propertyRepository.delete(userEntity);
                    return this.propertyMapper.toModel(userEntity);
                })
                .orElse(null);
    }

    public List<Property> listProperties() {
        return this.propertyRepository.findAll().stream().map(this.propertyMapper::toModel).toList();
    }
}
