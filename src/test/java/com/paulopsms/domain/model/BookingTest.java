package com.paulopsms.domain.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.paulopsms.domain.model.BookingStatus.CANCELLED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingTest {

    @Test
    public void givenBookingData_whenCreatingBooking_thenShouldCreateSuccessfully() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));

        Booking booking = new Booking(1L, property, user, dateRange, 2);

        assertEquals(1L, booking.getId());
        assertEquals(property, booking.getProperty());
        assertEquals(user, booking.getUser());
        assertEquals(dateRange, booking.getDateRange());
        assertEquals(2, booking.getNumberOfGuests());
    }

    @Test
    public void givenTwoOrMoreBookings_whenValidatingAvailability_thenShouldValidateAvailabilitySuccessfully() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));
        DateRange dateRange2 = new DateRange(LocalDate.parse("2025-04-26"), LocalDate.parse("2025-04-28"));
        DateRange dateRange3 = new DateRange(LocalDate.parse("2025-04-25"), LocalDate.parse("2025-04-30"));

        Booking booking = new Booking(1L, property, user, dateRange, 2);

        Boolean available = property.isAvailable(dateRange2);
        Boolean available2 = property.isAvailable(dateRange3);

        assertTrue(available);
        assertFalse(available2);
    }

    @Test
    public void givenABooking_whenValidatingGuests_thenShouldThrowAnExceptionIfGuestsLowerThanOrEqualsZero() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Booking(1L, property, user, dateRange, 0);
        });

        String expectedMessage = "O número de hóspedes deve ser maior que zero.";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }

    @Test
    public void givenABooking_whenValidatingGuests_thenShouldThrowAnExceptionIfGuestsHigherThanMaximumAllowed() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Booking(1L, property, user, dateRange, 6);
        });

        String expectedMessage = "O número máximo de hóspedes permitidos são " + property.getNumberOfGuests() + ".";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }

    @Test
    public void givenABooking_whenCalculatingPrice_thenShouldCalculateDiscount() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-30"));

        Booking booking = new Booking(1L, property, user, dateRange, 3);

        assertEquals(new BigDecimal("900.00"), booking.getTotalPrice());
    }

    @Test
    public void givenTwoOrMoreBookings_whenCalculatingPrice_thenShouldNotCalculateIfIsNotAvailable() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));
        Booking booking = new Booking(1L, property, user, dateRange, 2);

        DateRange dateRange2 = new DateRange(LocalDate.parse("2025-04-24"), LocalDate.parse("2025-04-28"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Booking(1L, property, user, dateRange2, 1);
        });

        String expectedMessage = "A propriedade não está disponível para o período selecionado.";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }

    @Test
    public void givenABooking_whenCancelingOneDayLeftForCheckin_thenShouldNotBePaidBack() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-30"));

        Booking booking = new Booking(1L, property, user, dateRange, 3);

        LocalDate currentDate = LocalDate.parse("2025-04-20");
        booking.cancel(currentDate);

        assertEquals(CANCELLED, booking.getBookingStatus());
        assertEquals(new BigDecimal("900.00"), booking.getTotalPrice());
    }

    @Test
    public void givenABooking_whenCancelingSevenDaysLeftForCheckin_thenShouldBePaidBackFullPrice() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-30"));

        Booking booking = new Booking(1L, property, user, dateRange, 3);

        LocalDate currentDate = LocalDate.parse("2025-04-10");
        booking.cancel(currentDate);

        assertEquals(CANCELLED, booking.getBookingStatus());
        assertEquals(new BigDecimal("0.00"), booking.getTotalPrice());
    }

    @Test
    public void givenABooking_whenCancelingBetweenOneAndSevenDaysLeftForCheckin_thenShouldBePaidBackHalfPrice() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-30"));

        Booking booking = new Booking(1L, property, user, dateRange, 3);

        LocalDate currentDate = LocalDate.parse("2025-04-15");
        booking.cancel(currentDate);

        assertEquals(CANCELLED, booking.getBookingStatus());
        assertEquals(new BigDecimal("450.00"), booking.getTotalPrice());
    }


    @Test
    public void givenABooking_whenCancelingAnAlreadyCancelledBooking_thenShouldThrowAnException() {
        Property property = new Property(1L, "Casa", "Descrição", 4, new BigDecimal("100.0"));
        User user = new User(1L, "Paulo Sérgio");
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-25"));
        Booking booking = new Booking(1L, property, user, dateRange, 2);
        booking.cancel(LocalDate.parse("2025-04-10"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            booking.cancel(LocalDate.parse("2025-04-11"));
        });

        String expectedMessage = "A reserva já está cancelada.";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }
}
