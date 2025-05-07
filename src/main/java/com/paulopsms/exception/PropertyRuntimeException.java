package com.paulopsms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PropertyRuntimeException extends RuntimeException {
    public PropertyRuntimeException(String s) {
        super(s);
    }
}
