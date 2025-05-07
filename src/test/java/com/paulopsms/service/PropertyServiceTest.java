package com.paulopsms.service;

import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Property;
import com.paulopsms.mapper.BookingMapper;
import com.paulopsms.mapper.PropertyMapper;
import com.paulopsms.repository.BookingRepository;
import com.paulopsms.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PropertyServiceTest {

    @InjectMocks
    private PropertyService propertyService;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    private PropertyEntity propertyEntity;
    private Property property;
    private Property property2;
    private PropertyEntity propertyEntity2;

    @BeforeEach
    public void createProperty() {
        propertyEntity = PropertyEntity.builder().id(1L).name("Casa de Praia").basePricePerNight(new BigDecimal("250.00")).build();
        property = Property.builder().id(1L).name("Casa de Praia").numberOfGuests(1).basePricePerNight(new BigDecimal("250.00")).build();
        propertyEntity2 = PropertyEntity.builder().id(3L).name("Apartamento").description("Ap 20").numberOfGuests(2).basePricePerNight(new BigDecimal("250")).build();
        property2 = Property.builder().id(3L).name("Apartamento").description("Ap 20").numberOfGuests(2).basePricePerNight(new BigDecimal("250")).build();
    }

    @Test
    public void givenPropertyId_whenFindingAnPropertyAndIdIsInvalid_thenShouldReturnNull() {
        Mockito.when(propertyRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(propertyMapper.toModel(Mockito.any(PropertyEntity.class))).thenReturn(null);

        Property property = propertyService.findPropertyById(999L);
        assertNull(property);
        verify(propertyRepository, times(1)).findById(Mockito.any());
        verify(propertyMapper, times(1)).toModel(Mockito.isNull());
    }

    @Test
    public void givenPropertyId_whenFindingAnPropertyAndIdIsValid_thenShouldReturnTheProperty() {
        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(propertyEntity));
        Mockito.when(propertyMapper.toModel(Mockito.any(PropertyEntity.class))).thenReturn(property);

        Property property = propertyService.findPropertyById(1L);
        assertNotNull(property);
        assertEquals(1L, property.getId());
        assertEquals("Casa de Praia", property.getName());

        verify(propertyRepository, times(1)).findById(Mockito.any());
        verify(propertyMapper, times(1)).toModel(Mockito.any(PropertyEntity.class));
    }

    @Test
    public void givenANewProperty_whenSavingProperty_thenShouldBeSavedSuccessfully() {
        PropertyEntity property = new PropertyEntity(3L, "Apartamento", "Ap 20", 2, new BigDecimal("250"));
        Mockito.when(propertyRepository.save(Mockito.any(PropertyEntity.class))).thenReturn(propertyEntity2);
        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(propertyEntity2));
        Mockito.when(propertyMapper.toModel(Mockito.any(PropertyEntity.class))).thenReturn(this.property2);

        this.propertyRepository.save(property);
        Property property2 = propertyService.findPropertyById(3L);

        assertNotNull(property2);
        assertEquals(property.getId(), property2.getId());
        assertEquals(property.getName(), property2.getName());
        verify(propertyRepository, times(1)).save(Mockito.any());
        verify(propertyRepository, times(1)).findById(Mockito.any());
        verify(propertyMapper, times(1)).toModel(Mockito.any(PropertyEntity.class));
    }
}
