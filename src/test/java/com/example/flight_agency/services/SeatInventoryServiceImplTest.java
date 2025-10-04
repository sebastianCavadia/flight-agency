package com.example.flight_agency.services;

import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.domine.repositories.SeatInventoryRepository;
import com.example.flight_agency.services.mappers.SeatInventoryMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeatInventoryServiceImplTest {
    @Mock
    private SeatInventoryRepository repo;

    @Mock
    private SeatInventoryMapper mapper;

    @InjectMocks
    private SeatInventoryServiceImpl service;

    @Test
    void reserveSeats_success() {
        when(repo.decrementAvailableSeatsIfEnough(1L, Cabin.ECONOMY, 2)).thenReturn(1);

        boolean result = service.reserveSeats(1L, Cabin.ECONOMY, 2);

        assertThat(result).isTrue();
        verify(repo).decrementAvailableSeatsIfEnough(1L, Cabin.ECONOMY, 2);
    }

    @Test
    void reserveSeats_failNotEnoughSeats() {
        when(repo.decrementAvailableSeatsIfEnough(1L, Cabin.BUSINESS, 5)).thenReturn(0);

        boolean result = service.reserveSeats(1L, Cabin.BUSINESS, 5);

        assertThat(result).isFalse();
        verify(repo).decrementAvailableSeatsIfEnough(1L, Cabin.BUSINESS, 5);
    }

    @Test
    void findById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

}