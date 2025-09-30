package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.entities.SeatInventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SeatInventoryRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private SeatInventoryRepository seatInventoryRepository;

    @Autowired
    private FlightRepository flightRepository;
    private Flight createAndSaveFlight() {
        Flight flight = Flight.builder()
                .number("AV123")
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();
        return flightRepository.save(flight);
    }


    @Test
    @DisplayName("Busca inventario de vuelos y cabina")
    void findByFlightIdAndCabin() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder().flight(flight).
                cabin(Cabin.ECONOMY).totalSeats(90).availableSeats(87).build();
        seatInventoryRepository.save(inventory);

        Optional<SeatInventory> foundInventory = seatInventoryRepository.findByFlightIdAndCabin(flight.getId(), Cabin.ECONOMY);

        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getFlight().getId()).isEqualTo(flight.getId());
        assertThat(foundInventory.get().getCabin()).isEqualTo(Cabin.ECONOMY);
    }

    @Test
    @DisplayName("Buscar si los asientos disponibles  esta en el minimo")
    void hasMinimumSeatsAvailable() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder().flight(flight).
                cabin(Cabin.BUSINESS).totalSeats(20).availableSeats(15).build();
        seatInventoryRepository.save(inventory);

        boolean available = seatInventoryRepository.
                hasMinimumSeatsAvailable(flight.getId(),Cabin.BUSINESS,5);


        assertThat(available).isTrue();
    }
}