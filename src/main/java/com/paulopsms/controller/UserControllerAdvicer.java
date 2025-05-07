package com.paulopsms.controller;

import com.paulopsms.exception.UserRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvicer {

    @ExceptionHandler(UserRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String userRuntimeExceptionHandler(UserRuntimeException ex) {
        return ex.getMessage();
    }
}
