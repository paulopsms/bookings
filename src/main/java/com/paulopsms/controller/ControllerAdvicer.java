package com.paulopsms.controller;

import com.paulopsms.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvicer {

    @ExceptionHandler(CancelBookingRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String cancelBookingRuntimeExceptionHandler(CancelBookingRuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DateRangeRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String dateRangeRuntimeExceptionHandler(DateRangeRuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ValidationRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String dateRangeRuntimeExceptionHandler(ValidationRuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(PropertyRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String propertyRuntimeExceptionHandler(PropertyRuntimeException ex) {
        return ex.getMessage();
    }
}
