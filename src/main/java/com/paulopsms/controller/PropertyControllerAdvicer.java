package com.paulopsms.controller;

import com.paulopsms.exception.PropertyRuntimeException;
import com.paulopsms.exception.UserRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PropertyControllerAdvicer {

    @ExceptionHandler(PropertyRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String propertyRuntimeExceptionHandler(PropertyRuntimeException ex) {
        return ex.getMessage();
    }
}
