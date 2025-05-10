package com.paulopsms.validation;

import com.paulopsms.domain.model.DateRange;
import com.paulopsms.domain.model.Property;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyValidationService {

    public void validateProperty(Property property) {
        this.validateName()
                .and(validateBasePricePerNight())
                .and(validatePositiveNumberOfGuests())
                .validate(property)
                .isValidOrElseThrow();
    }

    public void validatePropertyAvailability(Property property, DateRange dateRange) {
        this.validateAvailability(dateRange)
                .validate(property)
                .isValidOrElseThrow();
    }

    private Validator<Property> validateAvailability(DateRange dateRange) {
        return pprty -> Optional.ofNullable(pprty)
                .filter(p -> p.isAvailable(dateRange))
                .map(p -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "A propriedade não está disponível para o período selecionado."));
    }

    private Validator<Property> validateName() {
        return pprty -> Optional.ofNullable(pprty)
                .map(Property::getName)
                .filter(name -> !name.isBlank())
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "O nome da propriedade é obrigatório."));
    }

    private Validator<Property> validateBasePricePerNight() {
        return pprty -> Optional.ofNullable(pprty)
                .map(Property::getBasePricePerNight)
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "O Preço base por noite é obrigatório."));
    }

    private Validator<Property> validatePositiveNumberOfGuests() {
        return pprty -> Optional.ofNullable(pprty)
                .map(Property::getNumberOfGuests)
                .filter(numberOfGuests -> numberOfGuests > 0)
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "O número de hóspedes deve ser maior que zero."));
    }
}
