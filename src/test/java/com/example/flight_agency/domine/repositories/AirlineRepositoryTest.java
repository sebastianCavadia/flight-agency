package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.flight_agency.domine.entities.Airline;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

class AirlineRepositoryTest  extends AbstractRepositoryTest {
    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    @DisplayName("Buscar por codigo existente")
    void shouldFindAirlineByCodeIgnoreCase() {
        Airline airline = new Airline();
        airline.setName("Avianca");
        airline.setCode("AV");
        airlineRepository.save(airline);

        Optional<Airline> found = airlineRepository.findAirlineByCodeIgnoreCase("AV");

        assertThat(found).isPresent();
        assertThat(found.get().getCode()).isEqualTo("AV");
    }
    @Test
    @DisplayName("Buscar codigo que no existe")
    void shouldFindAirlineByNotExistCode() {
        Optional<Airline> notfoundAirline = airlineRepository.findAirlineByCodeIgnoreCase("zz");
        assertThat(notfoundAirline).isEmpty();
    }
}
