package com.paulopsms.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Property")
public class PropertyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Column(name = "base_price_per_night")
    private BigDecimal basePricePerNight;

//    @OneToMany(targetEntity = BookingEntity.class, mappedBy = "property")
//    private List<BookingEntity> bookings;

    public PropertyEntity(Long id, String name, String description, Integer numberOfGuests, BigDecimal basePricePerNight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfGuests = numberOfGuests;
        this.basePricePerNight = basePricePerNight;
    }
}
