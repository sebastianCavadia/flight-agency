package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE UPPER(b.passenger.email) = UPPER(:email) " +
            "ORDER BY b.createAt DESC")
    Page<Booking> findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc(
            @Param("email") String email,
            Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.passenger " +
            "LEFT JOIN FETCH b.items i " +
            "LEFT JOIN FETCH i.flight " +
            "WHERE b.id = :id")
    Optional<Booking> findByIdWithDetails(@Param("bookingId") Long bookingId);

    Optional<Booking> findBookingById(Long id);
}
