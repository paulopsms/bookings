package com.paulopsms.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paulopsms.exception.PropertyRuntimeException;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.paulopsms.domain.model.BookingStatus.CONFIRMED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Property implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer numberOfGuests;

    private BigDecimal basePricePerNight;

    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    public Property(Long id, String name, String description, Integer numberOfGuests, BigDecimal basePricePerNight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfGuests = numberOfGuests;
        this.basePricePerNight = basePricePerNight;

//        this.validatePropertyName();
//        this.validatePositiveNumberOfGuests();
//        this.validaBasePricePerNight();
    }

    public Property(Long id, String name, String description, Integer numberOfGuests, BigDecimal basePricePerNight, List<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfGuests = numberOfGuests;
        this.basePricePerNight = basePricePerNight;
        this.bookings = bookings;

//        this.validatePropertyName();
//        this.validatePositiveNumberOfGuests();
//        this.validaBasePricePerNight();
    }

//    private void validaBasePricePerNight() {
//        if (isNull(this.basePricePerNight)) throw new PropertyRuntimeException("O Preço base por noite é obrigatório.");
//    }
//
//    private void validatePositiveNumberOfGuests() {
//        if (this.numberOfGuests <= 0) throw new PropertyRuntimeException("O número de hóspedes deve ser maior que zero.");
//    }
//
//    public void validatePropertyName() {
//        if (nonNull(this.name) && this.name.isBlank()) throw new PropertyRuntimeException("O nome da propriedade é obrigatório.");
//    }

    public void validateMaximumNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests > this.numberOfGuests)
            throw new PropertyRuntimeException("O número máximo de hóspedes permitidos são " + this.numberOfGuests + ".");
    }

    public BigDecimal calculateTotalPrice(DateRange dateRange) {
        Long totalNights = dateRange.getTotalNights();

        BigDecimal totalPrice = BigDecimal.valueOf(totalNights).multiply(this.basePricePerNight);

        if (totalNights >= 7)
            totalPrice = totalPrice.multiply(new BigDecimal("0.9"));

        return totalPrice;
    }

    public void addBooking(Booking booking) {
        if (isNull(this.bookings)) this.bookings = new ArrayList<>();
        this.bookings.add(booking);
    }

    public Boolean isAvailable(DateRange dateRange) {
        if (isNull(this.bookings)) return true;
        return this.bookings.stream()
                .noneMatch(booking -> CONFIRMED.equals(booking.getBookingStatus()) && booking.getDateRange().overlaps(dateRange));
    }
}
