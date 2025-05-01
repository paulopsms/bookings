package com.paulopsms.service;

import com.paulopsms.domain.entity.Property;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PropertyServiceTest {

    @InjectMocks
    PropertyService propertyService;

    @Mock
    PropertyRepository propertyRepository;

    Property property;
    Property property2;
    @BeforeEach
    public void createProperty() {
        property = Property.builder().id(1L).name("Casa de Praia").build();
        property2 = Property.builder()
                .id(3L)
                .name("Apartamento")
                .description("Ap 20")
                .numberOfGuests(2)
                .basePricePerNight(new BigDecimal("250"))
                .build();
    }

    @Test
    public void givenPropertyId_whenFindingAnPropertyAndIdIsInvalid_thenShouldReturnNull() {
//        Mockito.when(propertyService.findPropertyById(1L)).thenReturn(this.property);
        Property property = propertyService.findPropertyById(999L);
        assertNull(property);
        verify(propertyRepository, times(1)).findById(Mockito.any());
    }

    @Test
    public void givenPropertyId_whenFindingAnPropertyAndIdIsValid_thenShouldReturnTheProperty() {
        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(property));
        Property property = propertyService.findPropertyById(1L);
        assertNotNull(property);
        assertEquals(1L, property.getId());
        assertEquals("Casa de Praia", property.getName());

        verify(propertyRepository, times(1)).findById(Mockito.any());
    }

    @Test
    public void givenANewProperty_whenSavingProperty_thenShouldBeSavedSuccessfully() {
        Property property = new Property(3L, "Apartamento", "Ap 20", 2, new BigDecimal("250"));
        Mockito.when(propertyRepository.save(Mockito.any(Property.class))).thenReturn(property2);
        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(property2));

        this.propertyRepository.save(property);
        Property property2 = propertyService.findPropertyById(3L);

        assertNotNull(property2);
        assertEquals(property.getId(), property2.getId());
        assertEquals(property.getName(), property2.getName());
        verify(propertyRepository, times(1)).save(Mockito.any());
        verify(propertyRepository, times(1)).findById(Mockito.any());
    }
}
