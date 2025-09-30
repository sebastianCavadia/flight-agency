package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Booking;
import com.example.flight_agency.domine.entities.BookingItem;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.entities.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Test
    @DisplayName("Buscar reservas de un pasajero por email")
    void findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc() {
        Passenger p = Passenger.builder().email("emailprueba@gamil.com").build();
        Passenger savedPassenger = passengerRepository.save(p);

        Booking booking1 = Booking.builder().passenger(savedPassenger).createAt(OffsetDateTime.now().minusDays(3)).build();
        Booking booking2 = Booking.builder().passenger(savedPassenger).createAt(OffsetDateTime.now().minusDays(2)).build();
        Booking booking3 = Booking.builder().passenger(savedPassenger).createAt(OffsetDateTime.now().minusDays(1)).build();
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        Pageable  pageable = PageRequest.of(0, 2);
        var result = bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreateAtDesc(
                "emailprueba@gamil.com", pageable);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getNumberOfElements()).isEqualTo(2);

        assertThat(result.getContent().get(0).getCreateAt()).isEqualTo(booking3.getCreateAt());
        assertThat(result.getContent().get(1).getCreateAt()).isEqualTo(booking2.getCreateAt());

    }

    @Test
    @DisplayName("Buscar reservas por Id")
    void findByIdWithDetails() {
        Flight flight = Flight.builder().build();
        Flight savedFlight = flightRepository.save(flight);

        Passenger p = Passenger.builder().fullName("Prueba de Oro").email("prueba@gamil.com").build();
        Passenger savedPassenger = passengerRepository.save(p);

        Booking booking = Booking.builder().passenger(savedPassenger).build();
        BookingItem bookingItem = BookingItem.builder().flight(flight).booking(booking).build();
        booking.addItem(bookingItem);
        Booking savedBooking = bookingRepository.save(booking);

        Optional<Booking> foundBooking = bookingRepository.findByIdWithDetails(savedBooking.getId());

        assertThat(foundBooking).isPresent();
        assertThat(foundBooking.get().getPassenger()).isNotNull();
        assertThat(foundBooking.get().getItems()).isNotEmpty();
        assertThat(foundBooking.get().getItems().getFirst().getFlight()).isNotNull();


    }
}