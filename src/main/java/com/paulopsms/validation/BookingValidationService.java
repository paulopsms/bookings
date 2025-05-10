package com.paulopsms.validation;

import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingValidationService {

    public void validateBooking(Booking booking) {
        this.validateNumberOfGuests()
//                .and(validatePropertyIsAvailable())
                .and(validateMaximumNumberOfGuests())
                .validate(booking)
                .isValidOrElseThrow();
    }

    private Validator<Booking> validateNumberOfGuests() {
        return booking -> Optional.ofNullable(booking)
                .map(Booking::getNumberOfGuests)
                .filter(numberOfGuests -> numberOfGuests > 0)
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "O número de hóspedes deve ser maior que zero."));
    }

    private Validator<Booking> validatePropertyIsAvailable() {
        return booking -> Optional.ofNullable(booking)
                .filter(b -> b.getProperty().isAvailable(b.getDateRange()))
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "A propriedade não está disponível para o período selecionado."));
    }

    private Validator<Booking> validateMaximumNumberOfGuests() {
        return booking -> Optional.ofNullable(booking)
                .filter(b -> b.getNumberOfGuests() <= b.getProperty().getNumberOfGuests())
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "O número máximo de hóspedes permitidos são " + booking.getProperty().getNumberOfGuests()));
    }
    
    
}
