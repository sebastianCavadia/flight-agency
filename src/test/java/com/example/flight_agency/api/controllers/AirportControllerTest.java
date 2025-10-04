package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.services.AirportService;
import com.example.flight_agency.api.error.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockitoBean
    AirportService service;

    @Test
    @DisplayName("POST /airports - Debe crear un aeropuerto y retornar 201 CREATED")
    void create_should() throws Exception {
        var req = new AirportDtos.AirportCreateRequest("BOG", "El Dorado", "Bogotá");
        var resp = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado", "Bogotá");

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("BOG"));
    }

    @Test
    @DisplayName("PUT /airports/{id} - Debe actualizar un aeropuerto y retornar 200 OK")
    void update_should() throws Exception {
        var req = new AirportDtos.AirportUpdateRequest("El Dorado T1", "Bogotá");
        var resp = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado T1", "Bogotá");

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(put("/api/v1/airports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("El Dorado T1"));
    }

    @Test
    @DisplayName("GET /airports/{id} - Debe retornar un aeropuerto por ID y 200 OK")
    void findById_should() throws Exception {
        var resp = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado", "Bogotá");
        when(service.findById(1L)).thenReturn(resp);

        mvc.perform(get("/api/v1/airports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BOG"))
                .andExpect(jsonPath("$.name").value("El Dorado"));
    }

    @Test
    @DisplayName("GET /airports/{id} - Debe retornar 404 NOT FOUND si el ID no existe")
    void findById_shouldReturn404WhenNotFound() throws Exception {
        // Asumiendo que 'NotFoundException' se mapea a 404 en GlobalExceptionHandler
        when(service.findById(9L)).thenThrow(new NotFoundException("Airport 9 not found"));

        mvc.perform(get("/api/v1/airports/9"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airport 9 not found"));
    }

    @Test
    @DisplayName("GET /airports/by-code/{code} - Debe retornar un aeropuerto por código y 200 OK")
    void findByCode_should() throws Exception {
        var resp = new AirportDtos.AirportResponse(2L, "MDE", "José María Córdova", "Medellín");
        when(service.findByCode("MDE")).thenReturn(resp);

        mvc.perform(get("/api/v1/airports/by-code/MDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("José María Córdova"));
    }

    @Test
    @DisplayName("GET /airports - Debe retornar la lista completa de aeropuertos y 200 OK")
    void findAll_shouldReturnList() throws Exception {
        var list = List.of(
                new AirportDtos.AirportResponse(1L, "BOG", "El Dorado", "Bogotá"),
                new AirportDtos.AirportResponse(2L, "MDE", "José María Córdova", "Medellín")
        );
        when(service.findAll()).thenReturn(list);

        mvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("BOG"))
                .andExpect(jsonPath("$[1].code").value("MDE"));
    }

    @Test
    @DisplayName("DELETE /airports/{id} - Debe eliminar un aeropuerto y retornar 204 NO CONTENT")
    void delete_should() throws Exception {
        mvc.perform(delete("/api/v1/airports/3"))
                .andExpect(status().isNoContent());
        verify(service).delete(3L);
    }
}