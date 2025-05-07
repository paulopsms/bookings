package com.paulopsms.domain.entity;

import com.paulopsms.domain.cancelation.RefundRule;
import com.paulopsms.domain.cancelation.RefundRuleFactory;
import com.paulopsms.domain.model.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.paulopsms.domain.model.BookingStatus.CANCELLED;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "Booking")
public class BookingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = PropertyEntity.class)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private PropertyEntity property;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToOne(targetEntity = DateRangeEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "date_range_id", referencedColumnName = "id")
    private DateRangeEntity dateRange;

    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @Column(name = "total_price")
    private BigDecimal totalPrice;
}
