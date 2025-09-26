package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
Optional<Passenger> findPassengerByEmailIgnoreCase(String email);

    @Query("" +
            "SELECT p " +
            "FROM Passenger p " +
            "LEFT JOIN FETCH p.passengerProfile " +
            "WHERE UPPER(p.email) = UPPER(:email)")
    Optional<Passenger> findPassengerByEmailIgnoreCaseJPQL(@Param("email") String email);
}
