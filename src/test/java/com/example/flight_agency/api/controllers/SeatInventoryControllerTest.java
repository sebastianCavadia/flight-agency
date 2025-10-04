package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.SeatInventoryDtos;
import com.example.flight_agency.api.error.NotFoundException;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.services.SeatInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatInventoryController.class)
class SeatInventoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SeatInventoryService service;

    // ---  Datos de Prueba ---
    private final String BASE_URL = "/api/v1/seat-inventories";
    private final Long INVENTORY_ID = 1L;
    private final Long FLIGHT_ID = 10L;
    private final Cabin TEST_CABIN = Cabin.ECONOMY;
    private final int TOTAL_SEATS = 150;
    private final int AVAILABLE_SEATS = 100;
    private final SeatInventoryDtos.SeatInventoryCreateRequest createRequest =
            new SeatInventoryDtos.SeatInventoryCreateRequest(FLIGHT_ID, TEST_CABIN, TOTAL_SEATS, AVAILABLE_SEATS);

    private final SeatInventoryDtos.SeatInventoryResponse responseDTO =
            new SeatInventoryDtos.SeatInventoryResponse(INVENTORY_ID, TEST_CABIN, TOTAL_SEATS, AVAILABLE_SEATS);

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST / - Debe crear el inventario de asientos y retornar 201 CREATED")
    void create_should() throws Exception {
        // Arrange
        when(service.create(any(SeatInventoryDtos.SeatInventoryCreateRequest.class))).thenReturn(responseDTO);
        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(INVENTORY_ID))
                .andExpect(jsonPath("$.cabin").value(TEST_CABIN.name()));

        verify(service).create(any(SeatInventoryDtos.SeatInventoryCreateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /{id} - Debe retornar el inventario por ID y 200 OK")
    void findById_should() throws Exception {
        when(service.findById(INVENTORY_ID)).thenReturn(responseDTO);
        mvc.perform(get(BASE_URL + "/{id}", INVENTORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(INVENTORY_ID))
                .andExpect(jsonPath("$.availableSeats").value(AVAILABLE_SEATS));

        verify(service).findById(INVENTORY_ID);
    }

    @Test
    @DisplayName("GET /{id} - Debe retornar 404 NOT FOUND si el ID no existe")
    void findById_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        final Long NON_EXISTENT_ID = 99L;
        final String ERROR_MESSAGE = "Inventory " + NON_EXISTENT_ID + " not found";

        doThrow(new NotFoundException(ERROR_MESSAGE)).when(service).findById(NON_EXISTENT_ID);

        // Act & Assert
        mvc.perform(get(BASE_URL + "/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(service).findById(NON_EXISTENT_ID);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /by-flight - Debe encontrar inventario por Flight ID y Cabin y retornar 200 OK")
    void findByFlightAndCabin_should() throws Exception {
        // Arrange
        when(service.findByFlightAndCabin(FLIGHT_ID, TEST_CABIN)).thenReturn(responseDTO);
        mvc.perform(get(BASE_URL + "/by-flight")
                        .param("flightId", String.valueOf(FLIGHT_ID))
                        .param("cabin", TEST_CABIN.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cabin").value(TEST_CABIN.name()));

        verify(service).findByFlightAndCabin(FLIGHT_ID, TEST_CABIN);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /has-min - Debe retornar true si hay asientos mínimos disponibles")
    void hasMin_should() throws Exception {
        final int MIN_SEATS = 50;
        when(service.hasMinimumSeatsAvailable(FLIGHT_ID, TEST_CABIN, MIN_SEATS)).thenReturn(true);

        mvc.perform(get(BASE_URL + "/has-min")
                        .param("flightId", String.valueOf(FLIGHT_ID))
                        .param("cabin", TEST_CABIN.name())
                        .param("min", String.valueOf(MIN_SEATS)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).hasMinimumSeatsAvailable(FLIGHT_ID, TEST_CABIN, MIN_SEATS);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST /reserve - Debe reservar asientos y retornar true")
    void reserve_should() throws Exception {
        // Arrange
        final int COUNT = 5;
        when(service.reserveSeats(FLIGHT_ID, TEST_CABIN, COUNT)).thenReturn(true);

        mvc.perform(post(BASE_URL + "/reserve")
                        .param("flightId", String.valueOf(FLIGHT_ID))
                        .param("cabin", TEST_CABIN.name())
                        .param("count", String.valueOf(COUNT)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).reserveSeats(FLIGHT_ID, TEST_CABIN, COUNT);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST /release - Debe liberar asientos y retornar 204 NO CONTENT")
    void release_should() throws Exception {
        // Arrange
        final int COUNT = 5;
        doNothing().when(service).releaseSeats(FLIGHT_ID, TEST_CABIN, COUNT);
        mvc.perform(post(BASE_URL + "/release")
                        .param("flightId", String.valueOf(FLIGHT_ID))
                        .param("cabin", TEST_CABIN.name())
                        .param("count", String.valueOf(COUNT)))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // No hay cuerpo

        verify(service).releaseSeats(FLIGHT_ID, TEST_CABIN, COUNT);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("PUT /{id} - Debe actualizar el inventario y retornar 200 OK")
    void update_should() throws Exception {
        final int NEW_TOTAL_SEATS = 200;
        final int NEW_AVAILABLE_SEATS = 150;
        final SeatInventoryDtos.SeatInventoryUpdateRequest updateRequest =
                new SeatInventoryDtos.SeatInventoryUpdateRequest(NEW_TOTAL_SEATS, NEW_AVAILABLE_SEATS);
        final SeatInventoryDtos.SeatInventoryResponse updatedResponse =
                new SeatInventoryDtos.SeatInventoryResponse(INVENTORY_ID, TEST_CABIN, NEW_TOTAL_SEATS, NEW_AVAILABLE_SEATS);

        when(service.update(eq(INVENTORY_ID), any(SeatInventoryDtos.SeatInventoryUpdateRequest.class)))
                .thenReturn(updatedResponse);
        mvc.perform(put(BASE_URL + "/{id}", INVENTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSeats").value(NEW_TOTAL_SEATS));

        verify(service).update(eq(INVENTORY_ID), any(SeatInventoryDtos.SeatInventoryUpdateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("DELETE /{id} - Debe eliminar el inventario y retornar 204 NO CONTENT")
    void delete_should() throws Exception {
        // Arrange
        doNothing().when(service).delete(INVENTORY_ID);
        mvc.perform(delete(BASE_URL + "/{id}", INVENTORY_ID))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // Sin contenido

        verify(service).delete(INVENTORY_ID);
    }
}