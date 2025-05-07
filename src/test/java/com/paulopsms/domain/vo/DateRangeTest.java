package com.paulopsms.domain.vo;

import com.paulopsms.domain.model.DateRange;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringBootConfiguration
public class DateRangeTest {

    @Test
    public void givenStartDateBeforeEndDate_whenCreateDataRange_thenAssertionSucceeds() {
        DateRange dateRange = new DateRange(LocalDate.parse("2025-04-13"), LocalDate.parse("2025-04-20"));

        assertEquals(LocalDate.parse("2025-04-13"), dateRange.getStartDate());
        assertEquals(LocalDate.parse("2025-04-20"), dateRange.getEndDate());
    }

    @Test
    public void givenDataTerminoBeforeDataInicio_whenExceptionThrown_thenAssertionSucceeds() {


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new DateRange(LocalDate.parse("2025-04-20"), LocalDate.parse("2025-04-13"));
        });

        String expectedMessage = "A data de término deve ser posterior à data de início.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void givenDataRange_whenCalculatingDaysBetween_thenAssertsSucceeds() {
        final LocalDate startDate = LocalDate.parse("2025-04-13");
        final LocalDate endDate = LocalDate.parse("2025-04-20");
        DateRange dateRange = new DateRange(startDate, endDate);

        final Long totalNights = dateRange.getTotalNights();

        Long expectedTotalNights = ChronoUnit.DAYS.between(startDate, endDate);

        assertEquals(expectedTotalNights, totalNights);
    }

    @Test
    public void givenTwoDateRanges_whenIntervalOverlaps_thenShouldNotBePossible() {
        final LocalDate startDate1 = LocalDate.parse("2025-04-13");
        final LocalDate endDate1 = LocalDate.parse("2025-04-20");

        final LocalDate startDate2 = LocalDate.parse("2025-04-18");
        final LocalDate endDate2 = LocalDate.parse("2025-04-23");

        final LocalDate startDate3 = LocalDate.parse("2025-04-10");
        final LocalDate endDate3 = LocalDate.parse("2025-04-14");

        DateRange dateRange1 = new DateRange(startDate1, endDate1);
        DateRange dateRange2 = new DateRange(startDate2, endDate2);
        DateRange dateRange3 = new DateRange(startDate3, endDate3);

        final Boolean overlaps = dateRange1.overlaps(dateRange2);
        final Boolean overlaps2 = dateRange2.overlaps(dateRange1);
        final Boolean overlaps3 = dateRange1.overlaps(dateRange3);
        final Boolean overlaps4 = dateRange2.overlaps(dateRange3);
        final Boolean overlaps5 = dateRange3.overlaps(dateRange1);

        assertTrue(overlaps);
        assertTrue(overlaps2);
        assertTrue(overlaps3);
        assertFalse(overlaps4);
        assertTrue(overlaps5);
    }

    @Test
    public void givenDateRange_whenStartDateEqualsToEndDate_thenThrowAnException() {
        final LocalDate startDate = LocalDate.parse("2025-04-20");
        final LocalDate endDate = LocalDate.parse("2025-04-20");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new DateRange(startDate, endDate);
        });

        String expectedMessage = "A data de término deve ser posterior à data de início.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }
}
