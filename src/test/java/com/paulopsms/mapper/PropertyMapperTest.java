package com.paulopsms.mapper;

import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Property;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PropertyMapperTest {

    @Autowired
    private PropertyMapper propertyMapper;

    private static Property property;
    private static PropertyEntity propertyEntity;
    private static PropertyEntity propertyEntityException;

    @BeforeAll
    public static void init() {
        property = Property.builder().id(1L).name("Teste1").description("Description").numberOfGuests(4).basePricePerNight(new BigDecimal("100.00")).build();
        propertyEntity = new PropertyEntity(1L, "Teste2", "Description", 4, new BigDecimal("100.00"));
        propertyEntityException = new PropertyEntity(1L, "", "Description", 4, new BigDecimal("100.00"));
    }

    @Test
    public void givenAnUserModel_whenConvertToUserEntity_thenShouldReturnUserEntity() {
        PropertyEntity entity = propertyMapper.toEntity(property);

        assertEquals(property.getId(), entity.getId());
        assertEquals(property.getName(), entity.getName());
    }

    @Test
    public void givenAnUserEntity_whenConvertToUserModel_thenShouldReturnUserModel() {
        Property userModel = propertyMapper.toModel(propertyEntity);

        assertEquals(propertyEntity.getId(), userModel.getId());
        assertEquals(propertyEntity.getName(), userModel.getName());
    }
}
