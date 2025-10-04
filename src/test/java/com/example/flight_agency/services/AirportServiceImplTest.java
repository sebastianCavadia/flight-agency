package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.repositories.AirportRepository;
import com.example.flight_agency.services.mappers.AirportMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private AirportServiceImpl airportService;

    /*
        @BeforeEach
        void setUp() {
            airportService = new AirportServiceImpl(airportRepository, airportMapper);
        }
    */
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
        AirportDtos.AirportResponse responseDto =
                new AirportDtos.AirportResponse(1L, "BOG", "El Dorado", "Bogotá");

        when(airportMapper.toEntity(request)).thenReturn(airport);
        when(airportRepository.save(any(Airport.class))).thenReturn(airport);
        when(airportMapper.toResponse(airport)).thenReturn(responseDto);
        when(airportRepository.findByCode("BOG")).thenReturn(Optional.of(airport));
        when(airportMapper.toResponse(airport)).thenReturn(responseDto);

        // crear
        AirportDtos.AirportResponse response = airportService.create(request);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.code()).isEqualTo("BOG");
        assertThat(response.name()).isEqualTo("El Dorado");

        AirportDtos.AirportResponse found = airportService.findByCode("BOG");
        assertThat(found.id()).isEqualTo(1L);

        verify(airportMapper).toEntity(request);
        verify(airportRepository).save(any(Airport.class));
        verify(airportRepository).findByCode("BOG");
        verify(airportMapper, times(2)).toResponse(airport);
    }

    @Test
    @DisplayName("Actualizar aeropuerto")
    void update() {

        Airport airport = Airport.builder()
                .id(2L)
                .code("MDE")
                .name("José María")
                .city("Medellín")
                .build();

        AirportDtos.AirportUpdateRequest updateRequest =
                new AirportDtos.AirportUpdateRequest("José Maria Update", "Medellín Update");
        AirportDtos.AirportResponse response =
                new AirportDtos.AirportResponse(2l, "MDE", "José María Updated", "Medellín Updated");

        when(airportRepository.findById(2L)).thenReturn(Optional.of(airport));
        doNothing().when(airportMapper).updateEntityFromRequest(eq(updateRequest), any(Airport.class));
        when(airportRepository.save(any(Airport.class))).thenReturn(airport);
        when(airportMapper.toResponse(airport)).thenReturn(response);
        AirportDtos.AirportResponse updated = airportService.update(2l, updateRequest);

        assertThat(updated.name()).isEqualTo("José María Updated");
        assertThat(updated.city()).isEqualTo("Medellín Updated");

        verify(airportRepository).findById(2L);
        verify(airportMapper).updateEntityFromRequest(eq(updateRequest), eq(airport));
        verify(airportRepository).save(eq(airport));
        verify(airportMapper).toResponse(eq(airport));
    }
}
