package com.paulopsms.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.paulopsms.exception.DateRangeRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRange implements Serializable {
    private Long id;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        this.validateDates();
    }

    private void validateDates() {
        if (!this.endDate.isAfter(this.startDate)) {
            throw new DateRangeRuntimeException("A data de término deve ser posterior à data de início.");
        }
    }

    @JsonIgnore
    public Long getTotalNights() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public Boolean overlaps(DateRange dateRange) {
        return !(this.endDate.isBefore(dateRange.getStartDate()) || this.startDate.isAfter(dateRange.getEndDate()));
    }
}
