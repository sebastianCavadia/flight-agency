package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Optional<Airline> findAirlineByCodeIgnoreCase(String code);

}

