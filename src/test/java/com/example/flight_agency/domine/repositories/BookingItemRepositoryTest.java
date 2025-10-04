package com.example.flight_agency.domine.repositories;


import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
class BookingItemRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingItemRepository bookingItemRepository;

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private  PassengerProfileRepository passengerProfileRepository;
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
    @DisplayName("Buscar items por id del booking y ordenarlos")
    void findByBookingIdOrderBySegmentOrder() {
        Passenger passenger = crearPassenger();
        Booking booking = Booking.builder()
                .createAt(OffsetDateTime.now())
                .passenger(passenger)
                .build();
        Booking saveBooking = bookingRepository.save(booking);

        Flight flight = crearFlight();

        BookingItem item1 = BookingItem.builder()
                .booking(saveBooking)
                .segmentOrder(4)
                .price(BigDecimal.valueOf(100))
                .cabin(Cabin.ECONOMY)
                .flight(flight).build();
        BookingItem item2 = BookingItem.builder()
                .booking(saveBooking)
                .segmentOrder(2)
                .price(BigDecimal.valueOf(50))
                .cabin(Cabin.BUSINESS)
                .flight(flight)
                .build();
        item1.setPrice(BigDecimal.valueOf(100));
        item2.setPrice(BigDecimal.valueOf(50));

        bookingItemRepository.saveAll(List.of(item1, item2));
        List<BookingItem> items = bookingItemRepository.findByBookingIdOrderBySegmentOrderAsc(saveBooking.getId());
        assertThat(items).isNotNull().hasSize(2);
        assertThat(items.get(0).getSegmentOrder()).isEqualTo(2);
        assertThat(items.get(1).getSegmentOrder()).isEqualTo(4);
    }

    @Test
    @DisplayName("Sumar todos los items")
    void calculateBookingTotal() {
        Passenger  passenger = crearPassenger();
        Flight flight = crearFlight();
        Booking booking = Booking.builder()
                .createAt(OffsetDateTime.now())
                .passenger(passenger)
                .build();
        Booking saveBooking = bookingRepository.save(booking);

        BookingItem i1 = BookingItem.builder().build();
        i1.setBooking(saveBooking);
        i1.setPrice(BigDecimal.valueOf(150));
        i1.setCabin(Cabin.ECONOMY);
        i1.setSegmentOrder(1);
        i1.setFlight(flight);


        BookingItem i2 = BookingItem.builder().build();
        i2.setBooking(saveBooking);
        i2.setPrice(BigDecimal.valueOf(250));
        i2.setCabin(Cabin.ECONOMY);
        i2.setSegmentOrder(2);
        i2.setFlight(flight);

        bookingItemRepository.saveAll(List.of(i1, i2));
        BigDecimal total = bookingItemRepository.calculateBookingTotal(saveBooking.getId());

        assertThat(total).isEqualByComparingTo("400");
    }

    @Test
    @DisplayName("Contar los asientos reservados")
    void countSeatsSoldForFlightAndCabin() {
        Passenger passenger = crearPassenger();
        Flight saveflight = crearFlight();
        Booking booking = Booking.builder()
                .createAt(OffsetDateTime.now())
                .passenger(passenger)
                .build();
        bookingRepository.save(booking);
        BookingItem i1 = BookingItem.builder()
                .booking(booking).
                flight(saveflight)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(100))
                .segmentOrder(1)
                .build();
        BookingItem i2 = BookingItem.builder()
                .booking(booking)
                .flight(saveflight)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(200))
                .segmentOrder(2).
                build();
        bookingItemRepository.saveAll(List.of(i1, i2));
        long cantAsientosReser = bookingItemRepository.countSeatsSoldForFlightAndCabin(saveflight.getId(),Cabin.BUSINESS);
        assertThat(cantAsientosReser).isEqualTo(2);
    }
}