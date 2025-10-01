package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.entities.SeatInventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
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
    @DisplayName("devuelve inventario existente")
    void findByFlightIdAndCabin_found() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(90)
                .availableSeats(87)
                .build();
        seatInventoryRepository.save(inventory);

        Optional<SeatInventory> foundInventory =
                seatInventoryRepository.findByFlightIdAndCabin(flight.getId(), Cabin.ECONOMY);

        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getFlight().getId()).isEqualTo(flight.getId());
        assertThat(foundInventory.get().getCabin()).isEqualTo(Cabin.ECONOMY);
    }

    @Test
    @DisplayName("Devuelve vacío si no existe inventario")
    void findByFlightIdAndCabin_notFound() {
        Flight flight = createAndSaveFlight();

        Optional<SeatInventory> foundInventory =
                seatInventoryRepository.findByFlightIdAndCabin(flight.getId(), Cabin.BUSINESS);

        assertThat(foundInventory).isEmpty();
    }

    @Test
    @DisplayName("Devuelve true cuando hay suficientes asientos")
    void hasMinimumSeatsAvailable_true() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .totalSeats(20)
                .availableSeats(15)
                .build();
        seatInventoryRepository.save(inventory);

        boolean available = seatInventoryRepository
                .hasMinimumSeatsAvailable(flight.getId(), Cabin.BUSINESS, 5);

        assertThat(available).isTrue();
    }

    @Test
    @DisplayName("Devuelve false cuando no hay suficientes asientos")
    void hasMinimumSeatsAvailable_false() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .totalSeats(20)
                .availableSeats(3)
                .build();
        seatInventoryRepository.save(inventory);

        boolean available = seatInventoryRepository
                .hasMinimumSeatsAvailable(flight.getId(), Cabin.BUSINESS, 5);

        assertThat(available).isFalse();
    }

    @Test
    @DisplayName("Resta asientos si hay suficientes")
    void decrementAvailableSeatsIfEnough_success() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.PREMIUM)
                .totalSeats(30)
                .availableSeats(10)
                .build();
        seatInventoryRepository.save(inventory);

        int updated = seatInventoryRepository
                .decrementAvailableSeatsIfEnough(flight.getId(), Cabin.PREMIUM, 5);

        assertThat(updated).isEqualTo(1);

        SeatInventory updatedInventory = seatInventoryRepository
                .findByFlightIdAndCabin(flight.getId(), Cabin.PREMIUM)
                .orElseThrow();

        assertThat(updatedInventory.getAvailableSeats()).isEqualTo(5);
    }

    @Test
    @DisplayName("No hace nada si no hay suficientes asientos")
    void decrementAvailableSeatsIfEnough_fail() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.PREMIUM)
                .totalSeats(30)
                .availableSeats(4)
                .build();
        seatInventoryRepository.save(inventory);

        int updated = seatInventoryRepository
                .decrementAvailableSeatsIfEnough(flight.getId(), Cabin.PREMIUM, 5);

        assertThat(updated).isEqualTo(0);

        SeatInventory updatedInventory = seatInventoryRepository
                .findByFlightIdAndCabin(flight.getId(), Cabin.PREMIUM)
                .orElseThrow();

        assertThat(updatedInventory.getAvailableSeats()).isEqualTo(4);
    }

    @Test
    @DisplayName("Suma asientos")
    void incrementAvailableSeats_success() {
        Flight flight = createAndSaveFlight();
        SeatInventory inventory = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(80)
                .build();
        seatInventoryRepository.save(inventory);

        int updated = seatInventoryRepository
                .incrementAvailableSeats(flight.getId(), Cabin.ECONOMY, 5);

        assertThat(updated).isEqualTo(1);

        SeatInventory updatedInventory = seatInventoryRepository
                .findByFlightIdAndCabin(flight.getId(), Cabin.ECONOMY)
                .orElseThrow();

        assertThat(updatedInventory.getAvailableSeats()).isEqualTo(85);
    }
}
