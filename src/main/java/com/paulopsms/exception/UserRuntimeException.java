package com.paulopsms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserRuntimeException extends RuntimeException {
    public UserRuntimeException(String s) {
        super(s);
    }
}
