package com.paulopsms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationRuntimeException extends RuntimeException {
    public ValidationRuntimeException(String s) {
        super(s);
    }
}
