package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.repositories.AirportRepository;
import com.example.flight_agency.services.mappers.AirportMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @Mock
    private AirportRepository airportRepository;

    private AirportMapper mapper = Mappers.getMapper(AirportMapper.class);

    @InjectMocks
    private AirportServiceImpl airportService;

    @BeforeEach
    void setUp() {
        airportService = new AirportServiceImpl(airportRepository, mapper);
    }

    @Test
    @DisplayName("Crear y buscar aeropuerto")
    void createAndFind() {
        AirportDtos.AirportCreateRequest request =
                new AirportDtos.AirportCreateRequest("BOG", "El Dorado", "Bogotá");

        Airport airport = Airport.builder()
                .id(1L)
                .code("BOG")
                .name("El Dorado")
                .city("Bogotá")
                .build();

        when(airportRepository.save(any(Airport.class))).thenReturn(airport);
        when(airportRepository.findByCode("BOG")).thenReturn(Optional.of(airport));

        // crear
        AirportDtos.AirportResponse response = airportService.create(request);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.code()).isEqualTo("BOG");
        assertThat(response.name()).isEqualTo("El Dorado");

        // buscar
        AirportDtos.AirportResponse found = airportService.findByCode("BOG");
        assertThat(found.id()).isEqualTo(1L);

        verify(airportRepository).save(any(Airport.class));
        verify(airportRepository).findByCode("BOG");
    }

    @Test
    @DisplayName("Actualizar aeropuerto")
    void update() {
        Airport existing = Airport.builder()
                .id(2L)
                .code("MDE")
                .name("José María")
                .city("Medellín")
                .build();

        when(airportRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(airportRepository.save(any(Airport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AirportDtos.AirportUpdateRequest updateReq =
                new AirportDtos.AirportUpdateRequest("José María Updated", "Medellín Updated");

        AirportDtos.AirportResponse updated = airportService.update(2L, updateReq);

        assertThat(updated.name()).isEqualTo("José María Updated");
        assertThat(updated.city()).isEqualTo("Medellín Updated");

        verify(airportRepository).findById(2L);
        verify(airportRepository).save(any(Airport.class));
    }
}
