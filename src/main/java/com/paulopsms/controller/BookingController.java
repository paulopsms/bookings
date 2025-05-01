package com.paulopsms.controller;

import com.paulopsms.domain.entity.Booking;
import com.paulopsms.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public @ResponseBody String getBookings() {
        List<Booking> bookings = bookingService.listBookings();

        log.info("Lista de Bookings: {}", bookings.size());
        bookings.forEach(booking -> log.info("Booking #{}", booking.getId()));

        return "Bookings in construction...";
    }
}
