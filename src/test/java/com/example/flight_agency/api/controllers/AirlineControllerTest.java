package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.services.AirlineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirlineController.class)
class AirlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AirlineService airlineService;

    // --- Datos de prueba ---
    private final AirlineDtos.AirlineCreateRequest createRequest =
            new AirlineDtos.AirlineCreateRequest("LA", "LATAM Airlines");

    private final AirlineDtos.AirlineResponse airlineResponse =
            new AirlineDtos.AirlineResponse(1L, "LA", "LATAM Airlines");

    private final String BASE_URL = "/api/v1/airlines";

    // --- Test para Crear Aerolínea (POST) ---
    @Test
    @DisplayName("POST /airlines - Debe crear una aerolínea y retornar 201 Created")
    void createAirline_should() throws Exception {
        when(airlineService.create(any(AirlineDtos.AirlineCreateRequest.class))).thenReturn(airlineResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("LATAM Airlines"))
                .andExpect(jsonPath("$.code").value("LA"));

        verify(airlineService).create(any(AirlineDtos.AirlineCreateRequest.class));
    }

    // --- Test para Buscar por ID (GET) ---
    @Test
    @DisplayName("GET /airlines/{id} - Debe retornar aerolínea por ID y status 200 OK")
    void getAirlineById_should() throws Exception {
        Long airlineId = 1L;
        when(airlineService.getById(airlineId)).thenReturn(airlineResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", airlineId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(airlineId))
                .andExpect(jsonPath("$.code").value("LA"));

        verify(airlineService).getById(airlineId);
    }

    // --- Test para Buscar por Código (GET) ---
    @Test
    @DisplayName("GET /airlines/by-code/{code} - Debe retornar aerolínea por código y status 200 OK")
    void getAirlineByCode_should() throws Exception {
        String airlineCode = "LA";
        when(airlineService.getByCode(airlineCode)).thenReturn(airlineResponse);

        mockMvc.perform(get(BASE_URL + "/by-code/{code}", airlineCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("LATAM Airlines"));

        verify(airlineService).getByCode(airlineCode);
    }
}
