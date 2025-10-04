package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.BookingItemDtos;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.services.BookingItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingItemController.class)
class BookingItemControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    BookingItemService service;

    @Test
    void create_should() throws Exception {
        var req = new BookingItemDtos.BookingItemCreateRequest(
                Cabin.ECONOMY, // o cualquier valor disponible en tu enum
                new BigDecimal("500.00"),
                1,
                22L
        );
        var resp = new BookingItemDtos.BookingItemResponse(
                10L,
                Cabin.ECONOMY,
                new BigDecimal("500.00"),
                1,
                null
        );

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/booking-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.price").value(500.00))
                .andExpect(jsonPath("$.segmentOrder").value(1));
    }

    @Test
    void findById_should() throws Exception {
        var resp = new BookingItemDtos.BookingItemResponse(
                5L,
                Cabin.BUSINESS,
                new BigDecimal("1500.00"),
                2,
                null
        );

        when(service.findById(5L)).thenReturn(resp);

        mvc.perform(get("/api/v1/booking-items/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.cabin").value("BUSINESS"))
                .andExpect(jsonPath("$.price").value(1500.00))
                .andExpect(jsonPath("$.segmentOrder").value(2));
    }

    @Test
    void findAll_should() throws Exception {
        var list = List.of(
                new BookingItemDtos.BookingItemResponse(1L, Cabin.ECONOMY, new BigDecimal("800.00"), 1, null),
                new BookingItemDtos.BookingItemResponse(2L, Cabin.BUSINESS, new BigDecimal("1200.50"), 2, null)
        );

        when(service.findAll()).thenReturn(list);

        mvc.perform(get("/api/v1/booking-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cabin").value("ECONOMY"))
                .andExpect(jsonPath("$[0].price").value(800.00))
                .andExpect(jsonPath("$[1].cabin").value("BUSINESS"))
                .andExpect(jsonPath("$[1].price").value(1200.50));
    }

    @Test
    void delete_should() throws Exception {
        mvc.perform(delete("/api/v1/booking-items/3"))
                .andExpect(status().isNoContent());
        verify(service).delete(3L);
    }
}