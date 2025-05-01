package com.paulopsms.domain.entity;

import com.paulopsms.domain.cancelation.RefundRule;
import com.paulopsms.domain.cancelation.RefundRuleFactory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.paulopsms.domain.entity.BookingStatus.CANCELLED;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "property")
    private Property property;

    @Column(name = "user")
    private User user;

    @Column(name = "dateRange")
    private DateRange dateRange;

    @Column(name = "numberOfGuests")
    private Integer numberOfGuests;

    @Column(name = "bookingStatus")
    private BookingStatus bookingStatus;

    @Column(name = "totalPrice")
    private BigDecimal totalPrice;



    public Booking(Long id, Property property, User user, DateRange dateRange, Integer numberOfGuests) {
        this.id = id;
        this.property = property;
        this.user = user;
        this.dateRange = dateRange;
        this.numberOfGuests = numberOfGuests;
        this.bookingStatus = BookingStatus.CONFIRMED;

        if (this.numberOfGuests <= 0) throw new RuntimeException("O número de hóspedes deve ser maior que zero.");

        this.property.validateMaximumNumberOfGuests(this.numberOfGuests);

        if (!this.property.isAvailable(this.dateRange))
            throw new RuntimeException("A propriedade não está disponível para o período selecionado.");

        this.totalPrice = this.property.calculateTotalPrice(this.dateRange);
        this.property.addBooking(this);
    }

    public void cancel(LocalDate currentDate) {
        if (CANCELLED.equals(this.bookingStatus)) throw new RuntimeException("A reserva já está cancelada.");

        RefundRule refundRule = RefundRuleFactory.getRefundRule(currentDate, this.getDateRange().getStartDate());
        this.totalPrice = refundRule.calculateRefund(this.totalPrice);
        this.bookingStatus = CANCELLED;
    }
}
