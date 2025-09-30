package com.example.flight_agency.domine.repositories;


import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
class BookingItemRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingItemRepository bookingItemRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    @DisplayName("Buscar equipajes por id del pasajero y ordenarlos")
    void findByBookingIdOrderBySegmentOrder() {
        Booking booking = Booking.builder().build();
        Booking saveBooking = bookingRepository.save(booking);

        BookingItem item1 = BookingItem.builder().booking(saveBooking).segmentOrder(4).build();
        BookingItem item2 = BookingItem.builder().booking(saveBooking).segmentOrder(2).build();
        item1.setPrice(BigDecimal.valueOf(100));
        item2.setPrice(BigDecimal.valueOf(50));

        bookingItemRepository.saveAll(List.of(item1, item2));
        List<BookingItem> items = bookingItemRepository.findByBookingId(saveBooking.getId());
        assertThat(items).isNotNull().hasSize(2);
        assertThat(items.get(0).getSegmentOrder()).isEqualTo(2);
        assertThat(items.get(1).getSegmentOrder()).isEqualTo(4);
    }

    @Test
    @DisplayName("Sumar todos los items")
    void calculateBookingTotal() {
        Booking booking = Booking.builder().build();
        Booking saveBooking = bookingRepository.save(booking);

        BookingItem i1 = BookingItem.builder().build();
        i1.setBooking(saveBooking);
        i1.setPrice(BigDecimal.valueOf(150));
        i1.setCabin(Cabin.ECONOMY);
        i1.setSegmentOrder(1);

        BookingItem i2 = BookingItem.builder().build();
        i2.setBooking(saveBooking);
        i2.setPrice(BigDecimal.valueOf(250));
        i2.setCabin(Cabin.ECONOMY);
        i2.setSegmentOrder(2);


        bookingItemRepository.saveAll(List.of(i1, i2));
        BigDecimal total = bookingItemRepository.calculateBookingTotal(saveBooking.getId());

        assertThat(total).isEqualByComparingTo("400");
    }

    @Test
    @DisplayName("Contar los asientos reservados")
    void countSeatsSoldForFlightAndCabin() {
        Flight flight = Flight.builder().build();
        Booking booking = Booking.builder().build();
        Flight saveFlight = flightRepository.save(flight);
        BookingItem i1 = BookingItem.builder().booking(booking).flight(saveFlight).
                cabin(Cabin.BUSINESS).price(BigDecimal.valueOf(100)).segmentOrder(1).build();
        BookingItem i2 = BookingItem.builder().booking(booking).flight(saveFlight).
                cabin(Cabin.BUSINESS).price(BigDecimal.valueOf(200)).segmentOrder(2).build();
        bookingItemRepository.saveAll(List.of(i1, i2));
        long cantAsientosReser = bookingItemRepository.countSeatsSoldForFlightAndCabin(saveFlight.getId(),Cabin.BUSINESS);
        assertThat(cantAsientosReser).isNotNull();
        assertThat(cantAsientosReser).isEqualTo(2);
    }
}