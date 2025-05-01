package com.paulopsms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateBookingDto {
    private Long propertyId;
    private Long guestId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer guestCount;
}
