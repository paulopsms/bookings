package com.paulopsms.service;

import com.paulopsms.domain.entity.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paulopsms.repository.PropertyRepository;

@Service
public class PropertyService {

    @Autowired
    PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property findPropertyById(Long propertyId) {
        return this.propertyRepository.findById(propertyId).orElse(null);
    }
}
