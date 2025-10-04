package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.services.PassegerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    PassegerService service;

    @Test
    void create_should() throws Exception {
        var profile = new PassengerDtos.PassengerProfileDto("+57", "CO");
        var req = new PassengerDtos.PassengerCreateRequest("Ana Ruiz", "ana@demo.com", profile);
        var resp = new PassengerDtos.PassengerResponse(
                10L, "Ana Ruiz", "ana@demo.com", profile, List.of()
        );

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.fullName").value("Ana Ruiz"))
                .andExpect(jsonPath("$.email").value("ana@demo.com"));
    }

    @Test
    void update_should() throws Exception {
        var profile = new PassengerDtos.PassengerProfileDto("+1", "US");
        var req = new PassengerDtos.PassengerUpdateRequest("Ana R.", "ana.new@demo.com", profile);
        var resp = new PassengerDtos.PassengerResponse(
                10L, "Ana R.", "ana.new@demo.com", profile, List.of()
        );

        when(service.update(eq(10L), any())).thenReturn(resp);

        mvc.perform(put("/api/v1/passengers/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.email").value("ana.new@demo.com"))
                .andExpect(jsonPath("$.profile.phone").value("+1"));
    }

    @Test
    void findById_should() throws Exception {
        var profile = new PassengerDtos.PassengerProfileDto("+34", "ES");
        var resp = new PassengerDtos.PassengerResponse(
                5L, "Luis Pérez", "luis@demo.com", profile, List.of()
        );

        when(service.findById(5L)).thenReturn(resp);

        mvc.perform(get("/api/v1/passengers/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.fullName").value("Luis Pérez"))
                .andExpect(jsonPath("$.profile.countryCode").value("ES"));
    }

    @Test
    void findByEmail_should() throws Exception {
        var profile = new PassengerDtos.PassengerProfileDto("+33", "FR");
        var resp = new PassengerDtos.PassengerResponse(
                7L, "Marie Curie", "marie@demo.com", profile, List.of()
        );

        when(service.findByEmail("marie@demo.com")).thenReturn(resp);

        mvc.perform(get("/api/v1/passengers/by-email")
                        .param("email", "marie@demo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Marie Curie"))
                .andExpect(jsonPath("$.profile.phone").value("+33"));
    }

    @Test
    void findAll_should() throws Exception {
        var passengerList = List.of(
                new PassengerDtos.PassengerResponseBasic(1L, "Ana Ruiz", "ana@demo.com"),
                new PassengerDtos.PassengerResponseBasic(2L, "Luis Pérez", "luis@demo.com")
        );

        when(service.findAll()).thenReturn(passengerList);

        mvc.perform(get("/api/v1/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Ana Ruiz"))
                .andExpect(jsonPath("$[1].email").value("luis@demo.com"));
    }
}