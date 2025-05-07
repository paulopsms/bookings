package com.paulopsms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulopsms.domain.model.Property;
import com.paulopsms.domain.model.User;
import com.paulopsms.exception.PropertyRuntimeException;
import com.paulopsms.exception.UserRuntimeException;
import com.paulopsms.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropertyController controller;

    @MockitoBean
    private PropertyService propertyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void assertThatControllerIsNotNull() {
        assertNotNull(controller);
    }

    @Test
    void givenPropertiesEndpoint_whenGettingProperties_thenShouldReturnListOfProperties() throws Exception {
        List<Property> properties = List.of(
                Property.builder().id(1L).name("Beach House").numberOfGuests(4).basePricePerNight(BigDecimal.valueOf(100.00)).build(),
                Property.builder().id(2L).name("Mountain Cabin").numberOfGuests(3).basePricePerNight(BigDecimal.valueOf(80.00)).build()
        );
        when(propertyService.listProperties()).thenReturn(properties);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/properties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        List<Property> returnedProperties = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(200, response.getStatus());
        assertNotNull(returnedProperties);
        assertEquals(2, returnedProperties.size());
    }

    @Test
    void givenPropertyId_whenFindingProperty_thenShouldReturnPropertyById() throws Exception {
        Property property = Property.builder()
                .id(1L)
                .name("Beach House")
                .numberOfGuests(4)
                .basePricePerNight(new BigDecimal("100.00"))
                .build();
        when(propertyService.findPropertyById(1L)).thenReturn(property);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/properties/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        Property returnedProperty = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(200, response.getStatus());
        assertNotNull(returnedProperty);
        assertEquals(1L, returnedProperty.getId());
        assertEquals("Beach House", returnedProperty.getName());
        assertEquals(4, returnedProperty.getNumberOfGuests());
        assertEquals(new BigDecimal("100.00"), returnedProperty.getBasePricePerNight());
    }


    @Test
    public void givenPropertyResource_whenCreatingNewProperty_thenShouldReturnCreatedProperty() throws Exception {
        Property propertyToCreate = Property.builder()
                .name("Lake House")
                .numberOfGuests(5)
                .basePricePerNight(BigDecimal.valueOf(120.00))
                .build();

        Property createdProperty = Property.builder()
                .id(3L)
                .name("Lake House")
                .numberOfGuests(5)
                .basePricePerNight(BigDecimal.valueOf(120.00))
                .build();

        when(propertyService.saveProperty(Mockito.any(Property.class))).thenReturn(createdProperty);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/properties")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(propertyToCreate)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        Property returnedProperty = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(201, response.getStatus());
        assertNotNull(returnedProperty);
        assertEquals(3L, returnedProperty.getId());
        assertEquals("Lake House", returnedProperty.getName());
        assertEquals(5, returnedProperty.getNumberOfGuests());
        assertEquals(BigDecimal.valueOf(120.00), returnedProperty.getBasePricePerNight());
    }

    @Test
    void givenPropertyId_whenRemovingProperty_thenShouldReturnNoContent() throws Exception {
        Property propertyToBeRemoved = Property.builder()
                .id(1L)
                .name("Beach House")
                .numberOfGuests(4)
                .basePricePerNight(new BigDecimal("100.00"))
                .build();

        Mockito.when(propertyService.removeProperty(1L)).thenReturn(propertyToBeRemoved);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/properties/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        Property removedProperty = objectMapper.readValue(contentAsString, new TypeReference<>() {});

        assertEquals(200, response.getStatus());
        assertEquals(1L, removedProperty.getId());
        Mockito.verify(propertyService, Mockito.times(1)).removeProperty(1L);
    }

    @Test
    public void givenAPropertyResource_withPropertyNumberOfGuestsLowerOrEqualsZero_thenShouldReturnBadRequestAndCaptureException() throws Exception {
        Property propertyToBeCreated = Property.builder()
                .id(1L)
                .name("Beach House")
                .numberOfGuests(2)
                .basePricePerNight(new BigDecimal("100.00"))
                .build();
        when(propertyService.saveProperty(Mockito.any(Property.class))).thenThrow(new PropertyRuntimeException("O número de hóspedes deve ser maior que zero."));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/properties")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(propertyToBeCreated)))
                .andExpect(result -> assertInstanceOf(PropertyRuntimeException.class, result.getResolvedException()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertEquals(BAD_REQUEST.value(), response.getStatus());
        String contentAsString = response.getContentAsString();
        assertEquals("O número de hóspedes deve ser maior que zero.", contentAsString);
    }

    @Test
    public void givenAPropertyResource_withPropertyNameMandatory_thenShouldReturnBadRequestAndCaptureException() throws Exception {
        Property propertyToBeCreated = Property.builder()
                .id(1L)
                .name("Beach House")
                .numberOfGuests(2)
                .basePricePerNight(new BigDecimal("100.00"))
                .build();
        when(propertyService.saveProperty(Mockito.any(Property.class))).thenThrow(new PropertyRuntimeException("O nome da propriedade é obrigatório."));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/properties")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(propertyToBeCreated)))
                .andExpect(result -> assertInstanceOf(PropertyRuntimeException.class, result.getResolvedException()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertEquals(BAD_REQUEST.value(), response.getStatus());
        String contentAsString = response.getContentAsString();
        assertEquals("O nome da propriedade é obrigatório.", contentAsString);
    }

    @Test
    public void givenAPropertyResource_withPropertyBasePricePerNightMandatory_thenShouldReturnBadRequestAndCaptureException() throws Exception {
        Property propertyToBeCreated = Property.builder()
                .id(1L)
                .name("Beach House")
                .numberOfGuests(2)
                .basePricePerNight(new BigDecimal("100.00"))
                .build();
        when(propertyService.saveProperty(Mockito.any(Property.class))).thenThrow(new PropertyRuntimeException("O nome da propriedade é obrigatório."));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/properties")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(propertyToBeCreated)))
                .andExpect(result -> assertInstanceOf(PropertyRuntimeException.class, result.getResolvedException()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertEquals(BAD_REQUEST.value(), response.getStatus());
        String contentAsString = response.getContentAsString();
        assertEquals("O nome da propriedade é obrigatório.", contentAsString);
    }
}
