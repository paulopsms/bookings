package com.paulopsms.mapper;

import com.paulopsms.domain.entity.BookingEntity;
import com.paulopsms.domain.entity.DateRangeEntity;
import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.DateRange;
import com.paulopsms.domain.model.Property;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.paulopsms.domain.model.BookingStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookingMapperTest {

    @Autowired
    private BookingMapper bookingMapper;

    private static Booking booking;
    private static BookingEntity bookingEntity;
    private static BookingEntity bookingEntityException;

    @BeforeAll
    public static void init() {
        booking = Booking.builder()
                .id(1L)
                .property(Property.builder()
                        .id(100L)
                        .name("Beach House")
                        .description("A beautiful beach house.")
                        .numberOfGuests(4)
                        .basePricePerNight(new BigDecimal("250.00"))
                        .bookings(new ArrayList<>())
                        .build())
                .numberOfGuests(2)
                .bookingStatus(CONFIRMED)
                .totalPrice(new BigDecimal("1000.00"))
                .dateRange(new DateRange(LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5)))
                .build();
        bookingEntity = BookingEntity.builder()
                .id(1L)
                .property(PropertyEntity.builder()
                        .id(100L)
                        .name("Beach House")
                        .description("A beautiful beach house.")
                        .numberOfGuests(4)
                        .basePricePerNight(new BigDecimal("250.00"))
//                        .bookings(new ArrayList<>())
                        .build())
                .dateRange(DateRangeEntity.builder()
                        .startDate(LocalDate.of(2023, 12, 1))
                        .endDate(LocalDate.of(2023, 12, 5))
                        .build())
                .numberOfGuests(2)
                .bookingStatus(CONFIRMED)
                .totalPrice(new BigDecimal("1000.00"))
                .build();
        bookingEntityException = BookingEntity.builder().id(1L)
                .property(PropertyEntity.builder()
                        .id(100L)
                        .name("Beach House")
                        .description("A beautiful beach house.")
                        .numberOfGuests(2)
                        .basePricePerNight(new BigDecimal("250.00"))
                        .build())
                .numberOfGuests(3)
                .build();
    }

    @Test
    public void givenAnBookingModel_whenConvertToBookingEntity_thenShouldReturnBookingEntity() {
        BookingEntity entity = bookingMapper.toEntity(booking);

        assertEquals(booking.getId(), entity.getId());
        assertEquals(booking.getNumberOfGuests(), entity.getNumberOfGuests());
        assertEquals(CONFIRMED, entity.getBookingStatus());
        assertEquals(booking.getTotalPrice(), entity.getTotalPrice());
    }

    @Test
    public void givenAnBookingEntity_whenConvertToBookingModel_thenShouldReturnBookingModel() {
        Booking bookingModel = bookingMapper.toModel(bookingEntity);

        assertEquals(bookingEntity.getId(), bookingModel.getId());
        assertEquals(bookingEntity.getNumberOfGuests(), bookingModel.getNumberOfGuests());
        assertEquals(CONFIRMED, bookingModel.getBookingStatus());
        assertEquals(bookingEntity.getTotalPrice(), bookingModel.getTotalPrice());
    }

    @Test
    public void givenAnEmptyBookingModelName_whenConvertToBookingEntity_thenShouldThrowAnException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingMapper.toModel(bookingEntityException);
        });

        String expectedMessage = "O número máximo de hóspedes permitidos são " + bookingEntityException.getProperty().getNumberOfGuests() + ".";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }
}
