package com.paulopsms.repository;

import com.paulopsms.domain.entity.BookingEntity;
import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findBookingByIdAndBookingStatus(Long id, BookingStatus status);

    List<BookingEntity> findAllByPropertyId(Long propertyId);


}
