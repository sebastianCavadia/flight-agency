package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Airport;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


class AirportRepositoryTest  extends AbstractRepositoryTest {
    @Autowired
    private AirportRepository airportRepository;

    @Test
    @DisplayName("Buscar aeropuerto por codigo")
    void shouldFindAirportByCode() {
        Airport airport = new Airport();
        airport.setName("El Dorado");
        airport.setCode("BOG");
        airportRepository.save(airport);

        Optional<Airport> foundAirport = airportRepository.findByCode("BOG");

        assertThat(foundAirport).isPresent();
        assertThat(foundAirport.get().getCode()).isEqualTo("BOG");
    }
}