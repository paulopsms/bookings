package com.paulopsms.validation;

import lombok.Getter;

public class ValidationResult {
    private final boolean isValid;

    @Getter
    private final String message;

    public ValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }
}
