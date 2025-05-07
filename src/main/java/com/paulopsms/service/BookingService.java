package com.paulopsms.service;

import com.paulopsms.domain.entity.BookingEntity;
import com.paulopsms.domain.model.*;
import com.paulopsms.dto.CreateBookingDto;
import com.paulopsms.exception.CancelBookingRuntimeException;
import com.paulopsms.mapper.BookingMapper;
import com.paulopsms.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingMapper bookingMapper;

    public BookingEntity findUserById(long bookingId) {
        return this.bookingRepository.findById(bookingId).get();
    }

    @Transactional
    public Booking createBooking(CreateBookingDto bookingDto) {
        Property property = this.propertyService.findPropertyById(bookingDto.getPropertyId());

        if (isNull(property)) throw new RuntimeException("Propriedade não encontrada.");

        User user = this.userService.findUserById(bookingDto.getGuestId());

        if (isNull(user)) throw new RuntimeException("Usuário não encontrado.");

        log.info("Creating DateRange");

        DateRange dateRange = new DateRange(bookingDto.getStartDate(), bookingDto.getEndDate());

        log.info("Creating Booking");

        Booking booking = new Booking(property, user, dateRange, bookingDto.getGuestCount());

        log.info("Mapping Booking to Entity");

        BookingEntity entity = this.bookingMapper.toEntity(booking);

        log.info("Saving BookingEntity {}", entity);

        this.bookingRepository.save(entity);

        log.info("Booking created with id {}", entity.getId());

        booking.getDateRange().setId(entity.getDateRange().getId());
        booking.setId(entity.getId());

        return booking;
    }

    public void cancelBooking(Long id) {
        Optional<BookingEntity> booking = this.bookingRepository.findBookingByIdAndBookingStatus(id, BookingStatus.CONFIRMED);

        booking.ifPresentOrElse(value -> {
            Booking model = bookingMapper.toModel(booking.get());
            model.cancel(LocalDate.now());
            BookingEntity entity = bookingMapper.toEntity(model);
            this.bookingRepository.save(entity);
        }, () -> {
            throw new CancelBookingRuntimeException("Reserva não encontrada.");
        });
    }

    public List<Booking> listBookings() {
        return this.bookingRepository.findAll().stream().map(this.bookingMapper::toModel).toList();
    }

    public Booking findBookingById(Long bookingId) {
        return this.bookingRepository.findById(bookingId).map(this.bookingMapper::toModel).orElse(null);
    }
}
