package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.api.dto.FlightDtos;
import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.services.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FlightService service;

    // ---  Datos de Prueba ---
    private final String BASE_URL = "/api/v1/flights";
    private final Long FLIGHT_ID = 1L;
    private final String FLIGHT_NUMBER = "LA800";
    private final String AIRLINE_NAME = "LATAM";
    private final String ORIGIN_CODE = "BOG";
    private final OffsetDateTime DEPARTURE = OffsetDateTime.parse("2025-10-10T15:00:00Z");
    private final OffsetDateTime ARRIVAL = OffsetDateTime.parse("2025-10-10T18:00:00Z");

    private FlightDtos.FlightCreateRequest createRequest;
    private FlightDtos.FlightResponse flightResponse;

    @BeforeEach
    void setUp() {
        AirlineDtos.AirlineResponse airlineResponse = new AirlineDtos.AirlineResponse(100L, "LA", AIRLINE_NAME);
        AirportDtos.AirportResponse originAirport = new AirportDtos.AirportResponse(200L, ORIGIN_CODE, "El Dorado", "Bogota");
        AirportDtos.AirportResponse destinationAirport = new AirportDtos.AirportResponse(300L, "MIA", "Miami Intl", "Miami");
        TagDtos.TagResponse tagResponse = new TagDtos.TagResponse(50L, "LONG_HAUL");

        // DTO de Creación (Request)
        this.createRequest = new FlightDtos.FlightCreateRequest(
                FLIGHT_NUMBER, DEPARTURE, ARRIVAL, airlineResponse.id(), originAirport.id(), destinationAirport.id()
        );
        this.flightResponse = new FlightDtos.FlightResponse(
                FLIGHT_ID, FLIGHT_NUMBER, DEPARTURE, ARRIVAL,
                airlineResponse, originAirport, destinationAirport, Set.of(tagResponse)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST /flights - Debe crear un vuelo y retornar 201 Created")
    void createFlight_should() throws Exception {
        when(service.create(any(FlightDtos.FlightCreateRequest.class))).thenReturn(flightResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FLIGHT_ID))
                .andExpect(jsonPath("$.number").value(FLIGHT_NUMBER))
                .andExpect(jsonPath("$.airline.name").value(AIRLINE_NAME));

        verify(service).create(any(FlightDtos.FlightCreateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /flights/{id} - Debe retornar un vuelo por ID y status 200 OK")
    void findFlightById_should() throws Exception {
        when(service.findById(FLIGHT_ID)).thenReturn(flightResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", FLIGHT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FLIGHT_ID))
                // Verifica DTO anidado del aeropuerto de origen
                .andExpect(jsonPath("$.origin.code").value(ORIGIN_CODE))
                // Verifica el Set de tags
                .andExpect(jsonPath("$.tags.length()").value(1));

        verify(service).findById(FLIGHT_ID);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /flights/by-airline - Debe retornar una lista de vuelos por nombre de aerolínea")
    void findByAirline_should() throws Exception {
        List<FlightDtos.FlightResponse> flightList = List.of(flightResponse);
        when(service.findByAirlineName(AIRLINE_NAME)).thenReturn(flightList);

        mockMvc.perform(get(BASE_URL + "/by-airline")
                        .param("airlineName", AIRLINE_NAME))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].number").value(FLIGHT_NUMBER));

        verify(service).findByAirlineName(AIRLINE_NAME);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /flights/search - Debe retornar una página de vuelos con parámetros de búsqueda válidos")
    void searchFlights_should() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<FlightDtos.FlightResponse> responsePage = new PageImpl<>(List.of(flightResponse), pageable, 1);

        String origin = ORIGIN_CODE;
        String destination = "MIA";
        String fechaInicio = "2025-10-01T00:00:00Z";
        String fechaSalida = "2025-11-01T00:00:00Z";

        when(service.searchFlights(
                eq(origin),
                eq(destination),
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                any(Pageable.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get(BASE_URL + "/search")
                        .param("origin", origin)
                        .param("destination", destination)
                        .param("fechaInicio", fechaInicio)
                        .param("fechaSalida", fechaSalida)
                        .param("page", "0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].number").value(FLIGHT_NUMBER));

        verify(service).searchFlights(
                eq(origin),
                eq(destination),
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                any(Pageable.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /flights/search - Debe retornar 400 BAD REQUEST si el formato de fecha es inválido")
    void searchFlights_shouldHandleDateTimeParseException() throws Exception {
        String origin = "BOG";
        String destination = "MIA";
        String fechaInicioInvalida = "2025/10/01";
        String fechaSalida = "2025-11-01T00:00:00Z";

        mockMvc.perform(get(BASE_URL + "/search")
                        .param("origin", origin)
                        .param("destination", destination)
                        .param("fechaInicio", fechaInicioInvalida)
                        .param("fechaSalida", fechaSalida))
                .andExpect(status().isBadRequest());
    }
}