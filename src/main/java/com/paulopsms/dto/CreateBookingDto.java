package com.paulopsms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateBookingDto {
    private Long propertyId;
    private Long guestId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer guestCount;
}
