package com.paulopsms.validation;

import com.paulopsms.exception.ValidationRuntimeException;
import lombok.Getter;

import java.util.Optional;

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

    public Optional<String> getFieldNameIfInvalid(String field) {
        return this.isValid ? Optional.empty() : Optional.of(field);
    }

    public Optional<Boolean> isValidOptional() {
        return Optional.of(isValid);
    }

    public void isValidOrElseThrow() {
        if (!isValid) throw new ValidationRuntimeException(this.message);
    }
}
