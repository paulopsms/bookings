package com.paulopsms.controller;

import com.paulopsms.exception.CancelBookingRuntimeException;
import com.paulopsms.exception.BookingRuntimeException;
import com.paulopsms.exception.DateRangeRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingControllerAdvicer {

    @ExceptionHandler(CancelBookingRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String cancelBookingRuntimeExceptionHandler(CancelBookingRuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BookingRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String unavailableBookingRuntimeExceptionHandler(BookingRuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DateRangeRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String dateRangeRuntimeExceptionHandler(DateRangeRuntimeException ex) {
        return ex.getMessage();
    }
}
