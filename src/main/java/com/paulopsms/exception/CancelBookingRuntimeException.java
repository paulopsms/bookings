package com.paulopsms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CancelBookingRuntimeException extends RuntimeException {
    public CancelBookingRuntimeException(String s) {
        super(s);
    }
}
