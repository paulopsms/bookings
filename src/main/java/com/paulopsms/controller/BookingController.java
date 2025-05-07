package com.paulopsms.controller;

import com.paulopsms.domain.model.Booking;
import com.paulopsms.dto.CreateBookingDto;
import com.paulopsms.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public @ResponseBody List<Booking> getBookings() {
        List<Booking> bookings = bookingService.listBookings();

        log.info("Lista de Bookings: {}", bookings.size());
        bookings.forEach(booking -> log.info("Booking #{}", booking.getId()));
        return bookings;
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Booking createProperty(@RequestBody CreateBookingDto bookingDto) {
        return this.bookingService.createBooking(bookingDto);
    }

    @GetMapping("/bookings/{id}")
    public @ResponseBody Booking findBooking(@PathVariable("id") Long bookingId) {
        return this.bookingService.findBookingById(bookingId);
    }

    @PutMapping("/bookings/{id}")
    public void cancelBooking(@PathVariable("id") Long bookingId) {
        this.bookingService.cancelBooking(bookingId);
    }
}
