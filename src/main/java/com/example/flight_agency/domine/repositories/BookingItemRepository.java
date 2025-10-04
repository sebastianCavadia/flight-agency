package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.BookingItem;
import com.example.flight_agency.domine.entities.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
    @Query("""
select bi from BookingItem bi where bi.booking.id = :bookingId order by bi.segmentOrder asc
""")
    List<BookingItem> findByBookingIdOrderBySegmentOrderAsc(@Param("bookingId") Long bookingId);

    @Query("SELECT COALESCE(SUM(bi.price), 0) FROM BookingItem bi " +
            "WHERE bi.booking.id = :bookingId")
    BigDecimal calculateBookingTotal(@Param("bookingId") Long bookingId);

    @Query("SELECT COUNT(bi) FROM BookingItem bi " +
            "WHERE bi.flight.id = :flightId AND bi.cabin = :cabin")
    long countSeatsSoldForFlightAndCabin(@Param("flightId") Long flightId,
                                         @Param("cabin") Cabin cabin);
}
