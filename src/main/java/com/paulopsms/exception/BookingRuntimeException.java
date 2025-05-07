package com.paulopsms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookingRuntimeException extends RuntimeException {
    public BookingRuntimeException(String s) {
        super(s);
    }
}
