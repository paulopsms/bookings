package com.paulopsms.validation;

@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T t);

    default Validator<T> and(Validator<? super T> other) {
        return obj -> {
            ValidationResult result = this.validate(obj);
            return !result.isValid() ? result : other.validate(obj);
        };
    }

    default Validator<T> or(Validator<? super T> other) {
        return obj -> {
            ValidationResult result = this.validate(obj);
            return result.isValid() ? result : other.validate(obj);
        };
    }

    default Validator<T> negate() {
        return obj -> {
            ValidationResult result = this.validate(obj);
            return new ValidationResult(!result.isValid(), result.getMessage());
        };
    }
}
