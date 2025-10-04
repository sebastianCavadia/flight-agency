package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private  FlightRepository flightRepository;
    @Autowired
    private PassengerProfileRepository passengerProfileRepository;

    private Passenger crearPassenger(){
        PassengerProfile passengerProfile = PassengerProfile.builder()
                .phone("322556589")
                .countryCode("+57")
                .build();
        PassengerProfile save = passengerProfileRepository.save(passengerProfile);
        Passenger passenger = Passenger.builder()
                .fullName("Pedro Marquez")
                .email("pedro123@gmail.com")
                .passengerProfile(save)
                .build();
        return passengerRepository.save(passenger);
    }

    private Flight crearFlight(){
        Flight flight = Flight.builder()
                .number("AT234")
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(2))
                .build();
        return flightRepository.save(flight);
    }

    @Test
    @DisplayName("Buscar reservas de un pasajero por email")
    void findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc() {
        Passenger p = crearPassenger();
        Booking booking1 = Booking.builder()
                .createAt(OffsetDateTime.now().minusDays(3))
                .passenger(p)
                .build();
        Booking booking2 = Booking.builder()
                .createAt(OffsetDateTime.now().minusDays(2))
                .passenger(p)
                .build();
        Booking booking3 = Booking.builder().createAt(OffsetDateTime.now().minusDays(1))
                .passenger(p)
                .build();
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        Pageable  pageable = PageRequest.of(0, 2);
        var result = bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreateAtDesc(
                "pedro123@gmail.com", pageable);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getNumberOfElements()).isEqualTo(2);

        assertThat(result.getContent().get(0).getCreateAt().getDayOfYear()).isEqualTo(booking3.getCreateAt().getDayOfYear());
        assertThat(result.getContent().get(1).getCreateAt().getDayOfYear()).isEqualTo(booking2.getCreateAt().getDayOfYear());

    }

    @Test
    @DisplayName("Buscar reservas por Id")
    void findByIdWithDetails() {
        Flight flight = crearFlight();
        Passenger passenger = crearPassenger();
        Booking booking = Booking.builder()
                .createAt(OffsetDateTime.now().minusDays(3))
                .passenger(passenger)
                .build();
        BookingItem bookingItem = BookingItem.builder()
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(100))
                .segmentOrder(1)
                .build();
        booking.addItem(bookingItem);
        Booking savedBooking = bookingRepository.save(booking);

        Optional<Booking> foundBooking = bookingRepository.findByIdWithDetails(savedBooking.getId());

        assertThat(foundBooking).isPresent();
        assertThat(foundBooking.get().getPassenger()).isNotNull();
        assertThat(foundBooking.get().getItems()).isNotEmpty();
        assertThat(foundBooking.get().getItems().getFirst().getFlight()).isNotNull();


    }
}