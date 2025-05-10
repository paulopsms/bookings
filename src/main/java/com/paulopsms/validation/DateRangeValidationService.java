package com.paulopsms.validation;

import com.paulopsms.domain.model.DateRange;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DateRangeValidationService {

    public void validateDateRange(DateRange dateRange) {
        this.validateEndDateIsAfterStartDate()
                .validate(dateRange)
                .isValidOrElseThrow();
    }

    private Validator<DateRange> validateEndDateIsAfterStartDate() {
        return dateRange -> Optional.ofNullable(dateRange)
                .filter(dr -> dr.getStartDate().isBefore(dr.getEndDate()))
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "A data de término deve ser posterior à data de início."));
    }
}
