package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.SeatInventoryDtos;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.entities.SeatInventory;
import com.example.flight_agency.domine.repositories.FlightRepository;
import com.example.flight_agency.domine.repositories.SeatInventoryRepository;
import com.example.flight_agency.services.mappers.SeatInventoryMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatInventoryServiceImplTest {
    @Mock
    private SeatInventoryRepository seatInventoryRepository;

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private SeatInventoryMapper seatInventoryMapper;

    @InjectMocks
    private SeatInventoryServiceImpl service;

    @Test
    @DisplayName("Reservar asientos")
    void reserveSeats_success() {
        when(seatInventoryRepository.decrementAvailableSeatsIfEnough(1L, Cabin.ECONOMY, 2)).thenReturn(1);

        boolean result = service.reserveSeats(1L, Cabin.ECONOMY, 2);

        assertThat(result).isTrue();
        verify(seatInventoryRepository).decrementAvailableSeatsIfEnough(1L, Cabin.ECONOMY, 2);
    }

    @Test
    @DisplayName("Verificar si no hay asientos suficientes al reservar")
    void reserveSeats_failNotEnoughSeats() {
        when(seatInventoryRepository.decrementAvailableSeatsIfEnough(1L, Cabin.BUSINESS, 5)).thenReturn(0);

        boolean result = service.reserveSeats(1L, Cabin.BUSINESS, 5);

        assertThat(result).isFalse();
        verify(seatInventoryRepository).decrementAvailableSeatsIfEnough(1L, Cabin.BUSINESS, 5);
    }
    @Test
    @DisplayName("Debe lanzar excepción si la cantidad al liberar es <= 0")
    void releaseSeats_invalidCount() {
        assertThatThrownBy(() -> service.releaseSeats(1L, Cabin.BUSINESS, 0))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(seatInventoryRepository);
    }
    @Test
    @DisplayName("Debe actualizar un SeatInventory correctamente")
    void update_success() {
        SeatInventory entity = SeatInventory.builder()
                .id(1L)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(80)
                .build();

        SeatInventoryDtos.SeatInventoryUpdateRequest req =
                new SeatInventoryDtos.SeatInventoryUpdateRequest(120, 90);

        SeatInventoryDtos.SeatInventoryResponse response =
                new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 120, 90);

        when(seatInventoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(seatInventoryRepository.save(entity)).thenReturn(entity);
        when(seatInventoryMapper.toResponse(entity)).thenReturn(response);

        var result = service.update(1L, req);

        assertThat(result.totalSeats()).isEqualTo(120);
        assertThat(result.availableSeats()).isEqualTo(90);

        verify(seatInventoryRepository).save(entity);
        verify(seatInventoryMapper).toResponse(entity);
    }
    @Test
    @DisplayName("Debe eliminar un SeatInventory por ID")
    void delete_success() {
        doNothing().when(seatInventoryRepository).deleteById(1L);

        service.delete(1L);

        verify(seatInventoryRepository).deleteById(1L);
    }
    @Test
    @DisplayName("Debe crear un SeatInventory asociado a un vuelo existente")
    void create_success() {
        var flight = Flight.builder()
                .number("AV123")
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(8))
                .build();
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        var req = new SeatInventoryDtos.SeatInventoryCreateRequest(1L, Cabin.ECONOMY, 100, 100);
        var entity = SeatInventory.builder()
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(100)
                .build();

        var response = new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 100, 100);

        when(seatInventoryRepository.save(any(SeatInventory.class))).thenReturn(entity);
        when(seatInventoryMapper.toResponse(entity)).thenReturn(response);

        var result = service.create(req);

        assertThat(result.cabin()).isEqualTo(Cabin.ECONOMY);
        verify(seatInventoryRepository).save(any(SeatInventory.class));
        verify(seatInventoryMapper).toResponse(entity);
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear si el vuelo no existe")
    void create_flightNotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
        var req = new SeatInventoryDtos.SeatInventoryCreateRequest(1L, Cabin.ECONOMY, 10, 10);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Flight no encontrado");
    }
}