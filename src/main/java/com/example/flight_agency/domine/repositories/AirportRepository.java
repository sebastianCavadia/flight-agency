package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
Optional<Airport> findByCode(String code);


}

