package com.paulopsms.domain.model;

import com.paulopsms.domain.cancelation.RefundRule;
import com.paulopsms.domain.cancelation.RefundRuleFactory;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.paulopsms.domain.model.BookingStatus.CANCELLED;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class Booking implements Serializable {
    private Long id;

    private Property property;

    private User user;

    private DateRange dateRange;

    private Integer numberOfGuests;

    private BookingStatus bookingStatus;

    private BigDecimal totalPrice;

    public Booking(Property property, User user, DateRange dateRange, Integer numberOfGuests) {
        this.property = property;
        this.user = user;
        this.dateRange = dateRange;
        this.numberOfGuests = numberOfGuests;
        this.bookingStatus = BookingStatus.CONFIRMED;

//        if (this.numberOfGuests <= 0) throw new BookingRuntimeException("O número de hóspedes deve ser maior que zero.");

//        this.property.validateMaximumNumberOfGuests(this.numberOfGuests);

//        if (!this.property.isAvailable(this.dateRange))
//            throw new BookingRuntimeException("A propriedade não está disponível para o período selecionado.");

        this.totalPrice = this.property.calculateTotalPrice(this.dateRange);
        this.property.addBooking(this);
    }

    public Booking(Long id, Property property, User user, DateRange dateRange, Integer numberOfGuests) {
        this.id = id;
        this.property = property;
        this.user = user;
        this.dateRange = dateRange;
        this.numberOfGuests = numberOfGuests;
        this.bookingStatus = BookingStatus.CONFIRMED;

//        if (this.numberOfGuests <= 0) throw new BookingRuntimeException("O número de hóspedes deve ser maior que zero.");

//        this.property.validateMaximumNumberOfGuests(this.numberOfGuests);

//        if (!this.property.isAvailable(this.dateRange))
//            throw new BookingRuntimeException("A propriedade não está disponível para o período selecionado.");

        this.totalPrice = this.property.calculateTotalPrice(this.dateRange);
        this.property.addBooking(this);
    }

    public Booking(Long id, Property property, User user, DateRange dateRange, Integer numberOfGuests, BookingStatus bookingStatus, BigDecimal totalPrice) {
        this.id = id;
        this.property = property;
        this.user = user;
        this.dateRange = dateRange;
        this.numberOfGuests = numberOfGuests;
        this.bookingStatus = bookingStatus;
        this.totalPrice = totalPrice;

//        if (this.numberOfGuests <= 0) throw new BookingRuntimeException("O número de hóspedes deve ser maior que zero.");

//        this.property.validateMaximumNumberOfGuests(this.numberOfGuests);

//        if (!this.property.isAvailable(this.dateRange))
//            throw new BookingRuntimeException("A propriedade não está disponível para o período selecionado.");

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
