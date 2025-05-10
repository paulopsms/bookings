package com.paulopsms.validation;

import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.DateRange;
import com.paulopsms.exception.ValidationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.paulopsms.domain.model.Property;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PropertyValidationServiceTest {

    @Autowired
    private PropertyValidationService propertyValidationService;

    @Test
    public void givenPropertyWithoutName_whenValidatingProperty_thenShouldThrowException() {
        Property property = Property.builder()
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(2)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                propertyValidationService.validateProperty(property)
        );

        String expectedMessage = "O nome da propriedade é obrigatório.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenPropertyWithoutNumberOfGuests_whenValidatingProperty_thenShouldThrowException() {
        Property property = Property.builder()
                .basePricePerNight(new BigDecimal("100.00"))
                .name("Valid Name")
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                propertyValidationService.validateProperty(property)
        );

        String expectedMessage = "O número de hóspedes deve ser maior que zero.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenPropertyWithoutBasePrice_whenValidatingProperty_thenShouldThrowException() {
        Property property = Property.builder()
                .name("Valid Name")
                .numberOfGuests(2)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                propertyValidationService.validateProperty(property)
        );

        String expectedMessage = "O Preço base por noite é obrigatório.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenPropertyWithNegativeGuests_whenValidatingProperty_thenShouldThrowException() {
        Property property = Property.builder()
                .name("Valid Name")
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(-1)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                propertyValidationService.validateProperty(property)
        );

        String expectedMessage = "O número de hóspedes deve ser maior que zero.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenValidProperty_whenValidatingProperty_thenShouldPassWithoutException() {
        Property property = Property.builder()
                .name("Valid Name")
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(2)
                .build();

        propertyValidationService.validateProperty(property);
    }

    @Test
    public void givenUnavailableProperty_whenValidatingAvailability_thenShouldThrowException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));
        Property property = Property.builder()
                .name("Unavailable Property")
                .basePricePerNight(new BigDecimal("150.00"))
                .numberOfGuests(2)
                .build();

        Booking booking = new Booking(new Property(1L,"name", "desc", 2, new BigDecimal("150.00")), null, dateRange, 2);

        property.setBookings(List.of(booking));

        RuntimeException exception = assertThrows(ValidationRuntimeException.class, () ->
                propertyValidationService.validatePropertyAvailability(property, dateRange)
        );

        String expectedMessage = "A propriedade não está disponível para o período selecionado.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenAvailableProperty_whenValidatingAvailability_thenShouldPassWithoutException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));
        DateRange dateRange2 = new DateRange(LocalDate.now().plusDays(6), LocalDate.now().plusDays(8));
        Property property = Property.builder()
                .name("Available Property")
                .basePricePerNight(new BigDecimal("200.00"))
                .numberOfGuests(2)
                .build();

        Booking booking = new Booking(new Property(1L,"name", "desc", 2, new BigDecimal("150.00")), null, dateRange, 2);

        property.setBookings(List.of(booking));

        propertyValidationService.validatePropertyAvailability(property, dateRange2);

        assertDoesNotThrow(() -> propertyValidationService.validatePropertyAvailability(property, dateRange2));
    }

}
