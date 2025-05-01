package com.paulopsms.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "numberOfGuests")
    private Integer numberOfGuests;

    @Column(name = "basePricePerNight")
    private BigDecimal basePricePerNight;

    private List<Booking> bookings = new ArrayList<>();

    public Property(Long id, String name, String description, Integer numberOfGuests, BigDecimal basePricePerNight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfGuests = numberOfGuests;
        this.basePricePerNight = basePricePerNight;

        if (this.name.isBlank()) throw new RuntimeException("O nome da propriedade é obrigatório.");
        if (this.numberOfGuests <= 0) throw new RuntimeException("O número de hóspedes deve ser maior que zero.");
    }

    public void validate() {
        if (this.name.isBlank()) throw new RuntimeException("O nome da propriedade é obrigatório.");
    }

    public void validateMaximumNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests > this.numberOfGuests)
            throw new RuntimeException("O número máximo de hóspedes permitidos são " + this.numberOfGuests + ".");
    }

    public BigDecimal calculateTotalPrice(DateRange dateRange) {
        Long totalNights = dateRange.getTotalNights();

        BigDecimal totalPrice = BigDecimal.valueOf(totalNights).multiply(this.basePricePerNight);

        if (totalNights >= 7)
            totalPrice = totalPrice.multiply(new BigDecimal("0.9"));

        return totalPrice;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public Boolean isAvailable(DateRange dateRange) {
        return !this.bookings.stream()
                .anyMatch(booking -> booking.getBookingStatus().equals(BookingStatus.CONFIRMED) && booking.getDateRange().overlaps(dateRange));
    }
}
