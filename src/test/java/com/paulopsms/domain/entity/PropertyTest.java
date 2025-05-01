package com.paulopsms.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PropertyTest {

    @Test
    public void givenPropertyData_whenCreatingNewProperty_thenShouldCreatePropertySuccessfully() {
        Property property = new Property(1L, "Casa de Praia", "Uma bela casa na praia", 4, new BigDecimal("200"));

        assertEquals(1L, property.getId());
        assertEquals("Casa de Praia", property.getName());
        assertEquals("Uma bela casa na praia", property.getDescription());
        assertEquals(4, property.getNumberOfGuests());
        assertEquals(new BigDecimal("200"), property.getBasePricePerNight());
    }

    @Test
    public void givenAnEmptyname_whenCreatingNewProperty_thenShouldThrowAnException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Property(1L, "", "Uma bela casa na praia", 0, new BigDecimal("200"));
        });

        String expectedMessage = "O nome da propriedade é obrigatório.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenNumberOfGuestsLowerThanOrEqualsZero_whenCreatingNewProperty_thenShouldThrowAnException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Property(1L, "Casa de Praia", "Uma bela casa na praia", 0, new BigDecimal("200"));
        });

        String expectedMessage = "O número de hóspedes deve ser maior que zero.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenNumberOfGuests_whenCreatingNewProperty_thenShouldValidateMaximumNumberOfGuests() {
        Property property = new Property(1L, "Casa de Praia", "Uma bela casa na praia", 4, new BigDecimal("150"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            property.validateMaximumNumberOfGuests(6);
        });

        String expectedMessage = "O número máximo de hóspedes permitidos são " + property.getNumberOfGuests() + ".";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenNumberOfNights_whenCalculatingDiscount_thenShouldNotGiveAnyDiscountIfLowerThanSeven() {
        Property property = new Property(1L, "Apartamento", "Ap 2 quartos", 2, new BigDecimal("100"));

        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));

        BigDecimal totalPrice = property.calculateTotalPrice(dateRange);

        assertEquals(new BigDecimal("500"), totalPrice);
    }

    @Test
    public void givenNumberOfNights_whenCalculatingDiscount_thenShouldGiveDiscountHigherThanSevenDays() {
        Property property = new Property(1L, "Apartamento", "Ap 2 quartos", 2, new BigDecimal("100"));

        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-30"));

        BigDecimal totalPrice = property.calculateTotalPrice(dateRange);

        assertEquals(new BigDecimal("900.0"), totalPrice);
    }
}
