package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.domine.entities.Booking;
import com.example.flight_agency.domine.entities.BookingItem;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.repositories.BookingRepository;
import com.example.flight_agency.domine.repositories.PassengerRepository;
import com.example.flight_agency.services.mappers.BookingMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("Crear y buscar reserva")
    void createAndFindBooking() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFullName("Test Passenger");

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setPassenger(passenger);

        BookingItem item = new BookingItem();
        item.setId(100L);
        item.setBooking(booking);
        booking.setItems(List.of(item));

        BookingDtos.BookingCreateRequest request = new BookingDtos.BookingCreateRequest(
                1L, // Passenger
                List.of(new BookingDtos.BookingItemCreateRequest(1L, Cabin.ECONOMY, 1))
        );

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        // Act
        BookingDtos.BookingResponse response = bookingService.create(request);

        // Assert
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.passenger().id()).isEqualTo(1L);
        assertThat(response.items()).hasSize(1);

        BookingDtos.BookingResponse found = bookingService.findById(10L);
        assertThat(found.id()).isEqualTo(10L);

        verify(passengerRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingRepository).findById(10L);
    }

    @Test
    @DisplayName("Buscar reservas por email")
    void findByPassengerEmail() {
        // Arrange
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");

        Booking booking = new Booking();
        booking.setId(20L);
        booking.setPassenger(passenger);

        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByPassengerEmail("test@example.com", PageRequest.of(0, 10)))
                .thenReturn(page);

        // Act
        var result = bookingService.findByPassengerEmail("test@example.com", PageRequest.of(0, 10));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(20L);

        verify(bookingRepository).findByPassengerEmail("test@example.com", PageRequest.of(0, 10));
    }
}