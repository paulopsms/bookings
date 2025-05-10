package com.paulopsms.validation;

import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.DateRange;
import com.paulopsms.domain.model.Property;
import com.paulopsms.domain.model.User;
import com.paulopsms.exception.ValidationRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingValidationServiceTest {

    @Autowired
    private BookingValidationService bookingValidationService;

    @MockitoBean
    private Property propertyMock;

    @MockitoBean
    private Booking bookingMock;

    @Test
    public void givenValidBooking_whenValidatingBooking_thenShouldPassWithoutException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));
        Property property = Property.builder()
                .name("Property")
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(2)
                .build();
        User user = User.builder().id(1L).name("John Doe").build();

        Booking booking = new Booking(property, user, dateRange, 2);

        assertDoesNotThrow(() -> bookingValidationService.validateBooking(booking));
    }

    @Test
    public void givenBookingWithZeroGuests_whenValidatingBooking_thenShouldThrowException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));
        Property property = Property.builder()
                .name("Property")
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(2)
                .build();
        User user = User.builder().id(1L).name("John Doe").build();

        Booking booking = new Booking(property, user, dateRange, 0);

        ValidationRuntimeException exception = assertThrows(ValidationRuntimeException.class,
                () -> bookingValidationService.validateBooking(booking));

        assertEquals("O número de hóspedes deve ser maior que zero.", exception.getMessage());
    }

    @Test
    public void givenBookingWithNegativeGuests_whenValidatingBooking_thenShouldThrowException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));
        Property property = Property.builder()
                .name("Property")
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(2)
                .build();
        User user = User.builder().id(1L).name("John Doe").build();

        Booking booking = new Booking(property, user, dateRange, -2);

        ValidationRuntimeException exception = assertThrows(ValidationRuntimeException.class,
                () -> bookingValidationService.validateBooking(bookingMock));

        assertEquals("O número de hóspedes deve ser maior que zero.", exception.getMessage());
    }

    @Test
    public void givenBookingWithGuestsExceedingPropertyLimit_whenValidatingBooking_thenShouldThrowException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));
        Property property = Property.builder()
                .name("Property")
                .basePricePerNight(new BigDecimal("100.00"))
                .numberOfGuests(4)
                .build();
        User user = User.builder().id(1L).name("John Doe").build();

        Booking booking = new Booking(property, user, dateRange, 5);

        ValidationRuntimeException exception = assertThrows(ValidationRuntimeException.class,
                () -> bookingValidationService.validateBooking(booking));

        assertEquals("O número máximo de hóspedes permitidos são " + property.getNumberOfGuests(), exception.getMessage());
    }
}
