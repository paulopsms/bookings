package com.paulopsms.service;

import com.paulopsms.domain.entity.Booking;
import com.paulopsms.domain.entity.Property;
import com.paulopsms.domain.entity.User;
import com.paulopsms.domain.entity.DateRange;
import com.paulopsms.dto.CreateBookingDto;
import com.paulopsms.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.paulopsms.domain.entity.BookingStatus.CANCELLED;
import static com.paulopsms.domain.entity.BookingStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class BookingServiceTest {

    @InjectMocks
    BookingService bookingService;

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    PropertyService mockPropertyService;

    @Mock
    UserService mockUserService;

    private Booking booking;
    private Property property;
    private User user;
    private CreateBookingDto bookingDto;
    // private Booking bookingCancelled;

    @BeforeEach
    public void create() {
        user = new User(1L, "Paulo");
        property = new Property(1L, "Casa", null, 4, new BigDecimal("500"));
        bookingDto = new CreateBookingDto(1L, 1L, LocalDate.parse("2025-05-21"), LocalDate.parse("2025-05-30"), 2);
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-25"), LocalDate.parse("2025-04-30"));
        booking = Booking.builder().id(1L).bookingStatus(CONFIRMED).dateRange(dateRange).totalPrice(new BigDecimal("4050.0")).build();
        // bookingCancelled = Booking.builder().id(1L).bookingStatus(CANCELLED).build();

    }

    @Test
    public void givenBookingData_whenCreatingABooking_thenShouldSaveOnFakeRepositorySuccessfully() {
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(this.property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(this.user);
        Mockito.when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking booking = bookingService.createBooking(bookingDto);

        assertInstanceOf(Booking.class, booking);
        assertEquals(CONFIRMED, booking.getBookingStatus());
        assertEquals(new BigDecimal("4050.0"), booking.getTotalPrice());

        Optional<Booking> savedBooking = mockBookingRepository.findById(booking.getId());
        assertNotNull(savedBooking);
        assertEquals(CONFIRMED, savedBooking.get().getBookingStatus());
        assertEquals(booking.getId(), savedBooking.get().getId());
    }

    @Test
    public void givenBookingDataWithInvalidPropertyId_whenCreatingABooking_thenShouldThrowAnException() {
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(bookingDto);
        });

        String expectedMessage = "Propriedade não encontrada.";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }

    @Test
    public void givenBookingDataWithInvalidUserId_whenCreatingABooking_thenShouldThrowAnException() {
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(this.property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(bookingDto);
        });

        String expectedMessage = "Usuário não encontrado.";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }

    @Test
    public void givenABooking_whenCancelling_thenShouldRemoveFromFakeRepositorySuccessfully() {
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(this.property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(this.user);
        Mockito.when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking booking = bookingService.createBooking(bookingDto);

        Booking bookingSpy = Mockito.spy(BookingRepository.class).save(booking);

        assertInstanceOf(Booking.class, booking);
        assertEquals(CONFIRMED, booking.getBookingStatus());
        assertEquals(new BigDecimal("4050.0"), booking.getTotalPrice());

        Optional<Booking> savedBooking = mockBookingRepository.findById(booking.getId());
        assertNotNull(savedBooking);
        assertEquals(CONFIRMED, savedBooking.get().getBookingStatus());
        assertEquals(booking.getId(), savedBooking.get().getId());

        bookingService.cancelBooking(booking.getId());

        Optional<Booking> cancelledBooking = mockBookingRepository.findById(booking.getId());

        assertEquals(CANCELLED, cancelledBooking.get().getBookingStatus());
        verify(mockBookingRepository, times(2)).save(Mockito.any());
    }
}
