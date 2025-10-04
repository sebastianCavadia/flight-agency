package com.example.flight_agency.services;


 import com.example.flight_agency.api.dto.AirlineDtos;
 import com.example.flight_agency.services.mappers.AirlineMapper;
 import com.example.flight_agency.domine.entities.Airline;
 import com.example.flight_agency.domine.repositories.AirlineRepository;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;
 import org.mockito.junit.jupiter.MockitoExtension;

 import java.util.Optional;

 import static org.assertj.core.api.Assertions.*;
 import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {
    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private AirlineMapper airlineMapper;
    @InjectMocks
    private AirlineServiceImpl airlineService;
    private Airline airline;
    private AirlineDtos.AirlineCreateRequest request;
    private AirlineDtos.AirlineResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        airline = Airline.builder()
                .id(1L)
                .code("AV")
                .name("Avianca")
                .build();

        request = new AirlineDtos.AirlineCreateRequest("AV", "Avianca");
        response = new AirlineDtos.AirlineResponse(1L, "AV", "Avianca");
    }

    @Test
    @DisplayName("Crear Airline con éxito")
    void createAirline() {
        when(airlineMapper.toEntity(request)).thenReturn(airline);
        when(airlineRepository.save(airline)).thenReturn(airline);
        when(airlineMapper.toResponse(airline)).thenReturn(response);

        AirlineDtos.AirlineResponse result = airlineService.create(request);

        assertThat(result).isEqualTo(response);
        verify(airlineRepository, times(1)).save(airline);
    }

    @Test
    @DisplayName("Buscar Airline por código con éxito")
    void findByCode() {
        when(airlineRepository.findAirlineByCodeIgnoreCase("AV")).thenReturn(Optional.of(airline));
        when(airlineMapper.toResponse(airline)).thenReturn(response);

        AirlineDtos.AirlineResponse result = airlineService.getByCode("AV");

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("Buscar Airline por id con éxito")
    void findById() {
        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airlineMapper.toResponse(airline)).thenReturn(response);

        AirlineDtos.AirlineResponse result = airlineService.getById(1L);

        assertThat(result).isEqualTo(response);
    }
}
