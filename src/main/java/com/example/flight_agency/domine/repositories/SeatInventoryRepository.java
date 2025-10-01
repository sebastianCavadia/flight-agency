package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.entities.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
    @Query("""
select s from SeatInventory
 s where s.flight.id = :flightId and s.cabin = :cabin
""")
    Optional<SeatInventory> findByFlightIdAndCabin(@Param("flightId") Long flightId,
                                                   @Param("cabin") Cabin cabin);
    @Query("SELECT CASE WHEN s.availableSeats >= :min " +
            "THEN true ELSE false END " +
            "FROM SeatInventory s WHERE s.flight.id = :flightId AND s.cabin = :cabin")
    boolean hasMinimumSeatsAvailable(
            @Param("flightId") Long flightId,
            @Param("cabin") Cabin cabin,
            @Param("min") int min);
    @Modifying
    @Query("UPDATE SeatInventory s SET s.availableSeats = s.availableSeats - :count " +
            "WHERE s.flight.id = :flightId AND s.cabin = :cabin AND s.availableSeats >= :count")
    int decrementAvailableSeatsIfEnough(@Param("flightId") Long flightId,
                                        @Param("cabin") Cabin cabin,
                                        @Param("count") int count);

    @Modifying
    @Query("UPDATE SeatInventory s SET s.availableSeats = s.availableSeats + :count " +
            "WHERE s.flight.id = :flightId AND s.cabin = :cabin")
    int incrementAvailableSeats(@Param("flightId") Long flightId,
                                @Param("cabin") Cabin cabin,
                                @Param("count") int count);
}
