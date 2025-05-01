package com.paulopsms.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DateRange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        this.validateDates();
    }

    private void validateDates() {
        if (!this.endDate.isAfter(this.startDate)) {
            throw new RuntimeException("A data de término deve ser posterior à data de início.");
        }
    }

    public Long getTotalNights() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public Boolean overlaps(DateRange dateRange) {
        return !(this.endDate.isBefore(dateRange.getStartDate()) || this.startDate.isAfter(dateRange.getEndDate()));
    }
}
