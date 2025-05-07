package com.paulopsms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DateRangeRuntimeException extends RuntimeException {
    public DateRangeRuntimeException(String s) {
        super(s);
    }
}
