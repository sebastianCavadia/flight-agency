package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.api.dto.BookingItemDtos;
import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.domine.entities.Booking;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.repositories.BookingRepository;
import com.example.flight_agency.domine.repositories.FlightRepository;
import com.example.flight_agency.domine.repositories.PassengerRepository;
import com.example.flight_agency.services.mappers.BookingMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BookingMapper mapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private  final OffsetDateTime FIXED_TIME = OffsetDateTime.now();
    @Test
    @DisplayName("Crear y buscar reserva")
    void createAndFindBooking() {
        // Arrange
        Passenger passenger = Passenger.builder()
                .id(1L)
                .fullName("Juan carlos camacho")
                .email("juan12camacho@gmail.com")
                .build();
        Flight flight = Flight.builder()
                .id(5L)
                .number("AA145")
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(5))
                .build();

        Booking booking = Booking.builder()
                .passenger(passenger)
                .createAt(FIXED_TIME)
                .items(new ArrayList<>())
                .build();
        Booking saveBookingEntity = Booking.builder()
                .id(10L)
                .passenger(passenger)
                .createAt(FIXED_TIME)
                .items(new ArrayList<>())
                .build();

        BookingDtos.BookingCreateRequest request = new BookingDtos.BookingCreateRequest(
                1L, // Passenger
                List.of(new BookingDtos.BookingItemCreateRequest(5L, Cabin.ECONOMY, 1))
        );

        PassengerDtos.PassengerResponseBasic passengerResponse =
                new PassengerDtos.PassengerResponseBasic(
                        1l,"Juan carlos camacho","juan12camacho@gmail.com");
        BookingItemDtos.BookingItemResponse itemResponse =
                new BookingItemDtos.BookingItemResponse(100L,Cabin.ECONOMY,new BigDecimal("100.00"),1,null);
        BookingDtos.BookingResponse bookingResponse =
                new BookingDtos.BookingResponse(10L, FIXED_TIME,passengerResponse,List.of(itemResponse));

        when(flightRepository.findById(5L)).thenReturn(Optional.of(flight));
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(mapper.toEntity(any(BookingDtos.BookingCreateRequest.class))).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(
                invocation -> {
                    Booking saveBooking = invocation.getArgument(0);
                    saveBooking.setId(10L);
                    saveBooking.setCreateAt(FIXED_TIME);
                    return saveBooking;
                });
        when(mapper.toResponse(any(Booking.class))).thenReturn(bookingResponse);
        when(bookingRepository.findByIdWithDetails(10L)).thenReturn(Optional.of(saveBookingEntity));

        // Act
        BookingDtos.BookingResponse response = bookingService.create(request);

        // Assert
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.passenger().id()).isEqualTo(1L);
        assertThat(response.items()).hasSize(1);

        BookingDtos.BookingResponse found = bookingService.findById(10L);
        assertThat(found.id()).isEqualTo(10L);

        verify(passengerRepository, times(1)).findById(1L);
        verify(mapper).toEntity(any(BookingDtos.BookingCreateRequest.class));
        verify(flightRepository).findById(5L);
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingRepository).findByIdWithDetails(10L);
        verify(mapper,times(2)).toResponse(any(Booking.class));
    }

    @Test
    @DisplayName("Buscar reservas por email")
    void findByPassengerEmail() {
        // Arrange
        Passenger passenger = Passenger.builder()
                .id(1L)
                .fullName("Pedro Carlos macha")
                .email("carlosmacha1@gmail.com")
                .build();

        Booking booking = Booking.builder()
                .id(20L)
                .passenger(passenger)
                .createAt(FIXED_TIME)
                .build();

        PassengerDtos.PassengerResponseBasic passengerResponse =
                new PassengerDtos.PassengerResponseBasic(1L,
                        "Pedro Carlos macha",
                        "carlosmacha1@gmail.com"
                        );
        BookingDtos.BookingResponse bookingResponse =
                new BookingDtos.BookingResponse(20L,
                        FIXED_TIME,passengerResponse,List.of());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking), pageable, 1);
        Page<BookingDtos.BookingResponse> bookingResponsePage = new PageImpl<>(List.of(bookingResponse), pageable, 1);

        when(bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreateAtDesc("carlosmacha1@gmail.com",pageable)).thenReturn(bookingPage);
        when(mapper.toResponse(booking)).thenReturn(bookingResponse);

        // Act
        var result = bookingService.findByPassengerEmail("carlosmacha1@gmail.com", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(20L);

        verify(bookingRepository).findByPassengerEmailIgnoreCaseOrderByCreateAtDesc("carlosmacha1@gmail.com",pageable);
        verify(mapper, times(1)).toResponse(booking);
    }
}