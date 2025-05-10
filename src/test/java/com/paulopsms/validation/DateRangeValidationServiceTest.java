package com.paulopsms.validation;

import com.paulopsms.domain.model.DateRange;
import com.paulopsms.exception.ValidationRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DateRangeValidationServiceTest {

    @Autowired
    private DateRangeValidationService dateRangeValidationService;

    @Test
    public void givenADateRange_whenEndDateBeforeStartDate_thenShouldThrowAnException() {
        DateRange dateRange = new DateRange(LocalDate.now().plusDays(5), LocalDate.now());

        RuntimeException exception = assertThrows(ValidationRuntimeException.class, () ->
                dateRangeValidationService.validateDateRange(dateRange));

        String expectedMessage = "A data de término deve ser posterior à data de início.";
        String message = exception.getMessage();

        assertInstanceOf(ValidationRuntimeException.class, exception);
        assertEquals(expectedMessage, message);
    }


    @Test
    public void givenAValidDateRange_whenEndDateAfterStartDate_thenShouldPassWithoutException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now().plusDays(5));

        dateRangeValidationService.validateDateRange(dateRange);
    }

    @Test
    public void givenAValidDateRange_whenStartDateEqualsEndDate_thenShouldShouldThrowAnException() {
        DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now());

        RuntimeException exception = assertThrows(ValidationRuntimeException.class, () ->
            dateRangeValidationService.validateDateRange(dateRange));

        String expectedMessage = "A data de término deve ser posterior à data de início.";
        String message = exception.getMessage();

        assertInstanceOf(ValidationRuntimeException.class, exception);
        assertEquals(expectedMessage, message);
    }
}
