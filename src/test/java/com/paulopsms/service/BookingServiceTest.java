package com.paulopsms.service;

import com.paulopsms.domain.entity.BookingEntity;
import com.paulopsms.domain.entity.DateRangeEntity;
import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.DateRange;
import com.paulopsms.domain.model.Property;
import com.paulopsms.domain.model.User;
import com.paulopsms.dto.CreateBookingDto;
import com.paulopsms.exception.CancelBookingRuntimeException;
import com.paulopsms.exception.ValidationRuntimeException;
import com.paulopsms.mapper.BookingMapper;
import com.paulopsms.repository.BookingRepository;
import com.paulopsms.validation.BookingValidationService;
import com.paulopsms.validation.DateRangeValidationService;
import com.paulopsms.validation.PropertyValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.paulopsms.domain.model.BookingStatus.CANCELLED;
import static com.paulopsms.domain.model.BookingStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository mockBookingRepository;

    @Mock
    private PropertyService mockPropertyService;

    @Mock
    private UserService mockUserService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingValidationService bookingValidationService;

    @Mock
    private DateRangeValidationService dateRangeValidationService;

    @Mock
    private PropertyValidationService propertyValidationService;

    private static Property property;
    private static PropertyEntity propertyEntity;
    private static User user;
    private static CreateBookingDto bookingDto;
    private static DateRange dateRange;
    private static DateRangeEntity dateRangeEntity;

    @BeforeEach
    public void create() {
        user = new User(1L, "Paulo");
        property = new Property(1L, "Casa", null, 4, new BigDecimal("500"));
        propertyEntity = new PropertyEntity(1L, "Casa", null, 4, new BigDecimal("500"));
        bookingDto = new CreateBookingDto(1L, 1L, LocalDate.parse("2025-05-21"), LocalDate.parse("2025-05-30"), 2);
        dateRange = new DateRange(LocalDate.parse("2025-04-21"), LocalDate.parse("2025-04-30"));
        dateRangeEntity = DateRangeEntity.builder().id(1L).startDate(LocalDate.parse("2025-04-21")).endDate(LocalDate.parse("2025-04-30")).build();
    }

    @Test
    public void givenBookingData_whenCreatingABooking_thenShouldSaveOnFakeRepositorySuccessfully() {
        Booking booking = Booking.builder().id(1L).property(property).bookingStatus(CONFIRMED).dateRange(dateRange).totalPrice(new BigDecimal("4050.0")).numberOfGuests(4).build();
        BookingEntity bookingEntity = BookingEntity.builder().id(1L).property(propertyEntity).bookingStatus(CONFIRMED).dateRange(dateRangeEntity).numberOfGuests(4).totalPrice(new BigDecimal("4050.0")).build();
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(user);
        Mockito.when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));
        Mockito.when(bookingMapper.toEntity(Mockito.any(Booking.class))).thenReturn(bookingEntity);

        Booking newBooking = bookingService.createBooking(bookingDto);

        assertInstanceOf(Booking.class, newBooking);
        assertEquals(CONFIRMED, newBooking.getBookingStatus());
        assertEquals(new BigDecimal("4050.0"), newBooking.getTotalPrice());

        Optional<BookingEntity> savedBooking = mockBookingRepository.findById(newBooking.getId());
        assertNotNull(savedBooking);
        assertEquals(CONFIRMED, savedBooking.get().getBookingStatus());
        assertEquals(newBooking.getId(), savedBooking.get().getId());
        verify(bookingMapper, times(1)).toEntity(Mockito.any(Booking.class));
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
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(property);
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
        Booking booking = Booking.builder().id(1L).property(property).bookingStatus(CONFIRMED).dateRange(dateRange).totalPrice(new BigDecimal("4050.0")).numberOfGuests(4).build();
        BookingEntity bookingEntity = BookingEntity.builder().id(1L).property(propertyEntity).bookingStatus(CONFIRMED).dateRange(dateRangeEntity).numberOfGuests(4).totalPrice(new BigDecimal("4050.0")).build();
        BookingEntity bookingCancelled = BookingEntity.builder().id(1L).property(propertyEntity).bookingStatus(CANCELLED).dateRange(dateRangeEntity).numberOfGuests(4).totalPrice(new BigDecimal("4050.0")).build();
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(user);
        Mockito.when(mockBookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity)).thenReturn(Optional.of(bookingCancelled));
        Mockito.when(mockBookingRepository.findBookingByIdAndBookingStatus(1L, CONFIRMED)).thenReturn(Optional.of(bookingEntity));
        Mockito.when(bookingMapper.toEntity(Mockito.any(Booking.class))).thenReturn(bookingEntity);
        Mockito.when(bookingMapper.toModel(Mockito.any(BookingEntity.class))).thenReturn(booking);

        Booking newBooking = bookingService.createBooking(bookingDto);

        assertInstanceOf(Booking.class, newBooking);
        assertEquals(CONFIRMED, newBooking.getBookingStatus());
        assertEquals(new BigDecimal("4050.0"), newBooking.getTotalPrice());

        Optional<BookingEntity> savedBooking = mockBookingRepository.findById(newBooking.getId());
        assertNotNull(savedBooking);
        assertEquals(CONFIRMED, savedBooking.get().getBookingStatus());
        assertEquals(newBooking.getId(), savedBooking.get().getId());

        bookingService.cancelBooking(booking.getId());

        Optional<BookingEntity> cancelledBooking = mockBookingRepository.findById(booking.getId());

        assertEquals(CANCELLED, cancelledBooking.get().getBookingStatus());
        verify(mockBookingRepository, times(2)).save(Mockito.any());
        verify(bookingMapper, times(2)).toEntity(Mockito.any(Booking.class));
        verify(bookingMapper, times(1)).toModel(Mockito.any(BookingEntity.class));
    }


    @Test
    public void givenNonExistingBooking_whenCancelling_thenShouldThrowException() {
        Long nonExistingBookingId = 999L;

        Mockito.when(mockBookingRepository.findBookingByIdAndBookingStatus(nonExistingBookingId, CONFIRMED))
                .thenReturn(Optional.empty());

        CancelBookingRuntimeException exception = assertThrows(CancelBookingRuntimeException.class, () -> {
            bookingService.cancelBooking(nonExistingBookingId);
        });

        String expectedMessage = "Reserva não encontrada.";
        String exceptionMessage = exception.getMessage();

        assertEquals(expectedMessage, exceptionMessage);
    }


    @Test
    public void givenValidDateRange_whenValidating_thenShouldPassWithoutException() {
        DateRange dateRange = new DateRange(LocalDate.parse("2025-05-21"), LocalDate.parse("2025-05-30"));

        assertDoesNotThrow(() -> dateRangeValidationService.validateDateRange(dateRange));
    }

    @Test
    public void givenInvalidDateRange_whenStartDateAfterEndDate_thenShouldThrowException() {
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(user);
        Mockito.doThrow(new ValidationRuntimeException("Data de início deve ser anterior à data de fim."))
                .when(dateRangeValidationService).validateDateRange(Mockito.any(DateRange.class));


        RuntimeException exception = assertThrows(ValidationRuntimeException.class, () -> {
            bookingService.createBooking(bookingDto);
        });

        assertInstanceOf(ValidationRuntimeException.class, exception);
        assertEquals("Data de início deve ser anterior à data de fim.", exception.getMessage());
        verify(mockPropertyService, times(1)).findPropertyById(Mockito.anyLong());
        verify(mockUserService, times(1)).findUserById(Mockito.anyLong());
        verify(dateRangeValidationService, times(1)).validateDateRange(Mockito.any(DateRange.class));
        verifyNoInteractions(propertyValidationService, bookingValidationService, bookingMapper, mockBookingRepository);
    }

    @Test
    public void givenProperty_whenNotAvailable_thenShouldThrowException() {
        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(user);
        Mockito.doNothing().when(dateRangeValidationService).validateDateRange(Mockito.any(DateRange.class));
        Mockito.doThrow(new ValidationRuntimeException("A propriedade não está disponível para o período selecionado."))
                .when(propertyValidationService).validatePropertyAvailability(Mockito.any(Property.class), Mockito.any(DateRange.class));
        ;

        RuntimeException exception = assertThrows(ValidationRuntimeException.class, () -> {
            bookingService.createBooking(bookingDto);
        });

        assertInstanceOf(ValidationRuntimeException.class, exception);
        assertEquals("A propriedade não está disponível para o período selecionado.", exception.getMessage());
        verify(mockPropertyService, times(1)).findPropertyById(Mockito.anyLong());
        verify(mockUserService, times(1)).findUserById(Mockito.anyLong());
        verify(dateRangeValidationService, times(1)).validateDateRange(Mockito.any(DateRange.class));
        verify(propertyValidationService, times(1)).validatePropertyAvailability(Mockito.any(Property.class), Mockito.any(DateRange.class));
        verifyNoInteractions(bookingValidationService, bookingMapper, mockBookingRepository);
    }

    @Test
    public void givenValidBooking_whenValidated_thenShouldPassWithoutException() {
        CreateBookingDto createBookingDto = new CreateBookingDto(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(9), 4);
        BookingEntity bookingEntity = BookingEntity.builder().id(1L).property(propertyEntity).bookingStatus(CONFIRMED).dateRange(dateRangeEntity).numberOfGuests(4).totalPrice(new BigDecimal("4050.0")).build();

        Mockito.when(mockPropertyService.findPropertyById(1L)).thenReturn(property);
        Mockito.when(mockUserService.findUserById(1L)).thenReturn(user);
        Mockito.doNothing().when(dateRangeValidationService).validateDateRange(Mockito.any(DateRange.class));
        Mockito.doNothing().when(propertyValidationService).validatePropertyAvailability(Mockito.any(Property.class), Mockito.any(DateRange.class));
        Mockito.doNothing().when(bookingValidationService).validateBooking(Mockito.any(Booking.class));
        Mockito.when(bookingMapper.toEntity(Mockito.any(Booking.class))).thenReturn(bookingEntity);

        Booking createdBooking = bookingService.createBooking(createBookingDto);

        assertNotNull(createdBooking);
        assertEquals(new BigDecimal("4050.0"), createdBooking.getTotalPrice());
        assertEquals(CONFIRMED, createdBooking.getBookingStatus());
        assertEquals(1L, createdBooking.getId());
        assertEquals(bookingEntity.getNumberOfGuests(), createdBooking.getNumberOfGuests());
        assertEquals("Casa", createdBooking.getProperty().getName());
        assertEquals(new BigDecimal("500"), createdBooking.getProperty().getBasePricePerNight());
        assertEquals(LocalDate.now(), createdBooking.getDateRange().getStartDate());
        assertEquals(LocalDate.now().plusDays(9), createdBooking.getDateRange().getEndDate());
        assertEquals(propertyEntity.getId(), createdBooking.getProperty().getId());
        assertEquals(propertyEntity.getName(), createdBooking.getProperty().getName());
        assertEquals(propertyEntity.getNumberOfGuests(), createdBooking.getProperty().getNumberOfGuests());
        assertEquals(propertyEntity.getBasePricePerNight(), createdBooking.getProperty().getBasePricePerNight());
        verify(mockPropertyService, times(1)).findPropertyById(Mockito.anyLong());
        verify(mockUserService, times(1)).findUserById(Mockito.anyLong());
        verify(dateRangeValidationService, times(1)).validateDateRange(Mockito.any(DateRange.class));
        verify(propertyValidationService, times(1)).validatePropertyAvailability(Mockito.any(Property.class), Mockito.any(DateRange.class));
        verify(bookingValidationService, times(1)).validateBooking(Mockito.any(Booking.class));
        verify(bookingMapper, times(1)).toEntity(Mockito.any(Booking.class));
        verify(mockBookingRepository, times(1)).save(Mockito.any(BookingEntity.class));
    }
}
