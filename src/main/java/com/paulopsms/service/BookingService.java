package com.paulopsms.service;

import com.paulopsms.domain.entity.Booking;
import com.paulopsms.domain.entity.Property;
import com.paulopsms.domain.entity.User;
import com.paulopsms.domain.entity.DateRange;
import com.paulopsms.dto.CreateBookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paulopsms.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    PropertyService propertyService;

    @Autowired
    UserService userService;

//    public BookingService(BookingRepository bookingRepository) {
//        this.bookingRepository = bookingRepository;
//    }

    public Booking findUserById(long bookingId) {
        return this.bookingRepository.findById(bookingId).get();
    }

    public Booking createBooking(CreateBookingDto bookingDto) {
        Property property = this.propertyService.findPropertyById(bookingDto.getPropertyId());

        if (isNull(property)) throw new RuntimeException("Propriedade não encontrada.");

        User user = this.userService.findUserById(bookingDto.getGuestId());

        if (isNull(user)) throw new RuntimeException("Usuário não encontrado.");

        DateRange dateRange = new DateRange(bookingDto.getStartDate(), bookingDto.getEndDate());

        Booking booking = new Booking(1L, property, user, dateRange, bookingDto.getGuestCount());

        this.bookingRepository.save(booking);

        return booking;
    }

    public void cancelBooking(Long id) {
        Optional<Booking> booking = this.bookingRepository.findById(id);

        booking.ifPresent(value -> {
            value.cancel(LocalDate.now());
            this.bookingRepository.save(value);
        });
    }

    public List<Booking> listBookings() {
        return this.bookingRepository.findAll();
    }
}
