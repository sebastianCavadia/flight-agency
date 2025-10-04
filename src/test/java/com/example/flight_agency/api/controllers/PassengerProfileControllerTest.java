package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.PassengerProfileDtos;
import com.example.flight_agency.api.error.NotFoundException;
import com.example.flight_agency.services.PassengerProfileService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerProfileController.class)
class PassengerProfileControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PassengerProfileService service;

    private final String BASE_URL = "/api/v1/passenger-profiles";
    private final Long PROFILE_ID = 1L;
    private final String PHONE_INITIAL = "3001234567";
    private final String COUNTRY_CODE_INITIAL = "+57";

    private final PassengerProfileDtos.PassengerProfileCreateRequest createRequest =
            new PassengerProfileDtos.PassengerProfileCreateRequest(PHONE_INITIAL, COUNTRY_CODE_INITIAL);

    private final PassengerProfileDtos.PassengerProfileResponse responseDTO =
            new PassengerProfileDtos.PassengerProfileResponse(PROFILE_ID, PHONE_INITIAL, COUNTRY_CODE_INITIAL);


    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST /passenger-profiles - Debe crear un perfil y retornar 201 CREATED")
    void create_should() throws Exception {
        // Arrange
        when(service.create(any(PassengerProfileDtos.PassengerProfileCreateRequest.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PROFILE_ID))
                .andExpect(jsonPath("$.phone").value(PHONE_INITIAL));

        verify(service).create(any(PassengerProfileDtos.PassengerProfileCreateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /passenger-profiles/{id} - Debe retornar un perfil por ID y 200 OK")
    void findById_should() throws Exception {
        // Arrange
        when(service.findById(PROFILE_ID)).thenReturn(responseDTO);

        // Act & Assert
        mvc.perform(get(BASE_URL + "/{id}", PROFILE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PROFILE_ID))
                .andExpect(jsonPath("$.countryCode").value(COUNTRY_CODE_INITIAL));

        verify(service).findById(PROFILE_ID);
    }

    @Test
    @DisplayName("GET /passenger-profiles/{id} - Debe retornar 404 NOT FOUND si el perfil no existe")
    void findById_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        final Long NON_EXISTENT_ID = 99L;
        final String ERROR_MESSAGE = "Passenger Profile " + NON_EXISTENT_ID + " not found";

        doThrow(new NotFoundException(ERROR_MESSAGE))
                .when(service).findById(NON_EXISTENT_ID);

        // Act & Assert
        mvc.perform(get(BASE_URL + "/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(service).findById(NON_EXISTENT_ID);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("PUT /passenger-profiles/{id} - Debe actualizar el perfil y retornar 200 OK")
    void update_should() throws Exception {
        final String PHONE_UPDATED = "3009998877";
        final PassengerProfileDtos.PassengerProfileUpdateRequest updateRequest =
                new PassengerProfileDtos.PassengerProfileUpdateRequest(PHONE_UPDATED, COUNTRY_CODE_INITIAL);
        final PassengerProfileDtos.PassengerProfileResponse updatedResponse =
                new PassengerProfileDtos.PassengerProfileResponse(PROFILE_ID, PHONE_UPDATED, COUNTRY_CODE_INITIAL);

        when(service.update(eq(PROFILE_ID), any(PassengerProfileDtos.PassengerProfileUpdateRequest.class)))
                .thenReturn(updatedResponse);

        mvc.perform(put(BASE_URL + "/{id}", PROFILE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value(PHONE_UPDATED));

        verify(service).update(eq(PROFILE_ID), any(PassengerProfileDtos.PassengerProfileUpdateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("DELETE /passenger-profiles/{id} - Debe eliminar el perfil y retornar 204 NO CONTENT")
    void delete_should() throws Exception {
        // Arrange
        doNothing().when(service).delete(PROFILE_ID);

        mvc.perform(delete(BASE_URL + "/{id}", PROFILE_ID))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service).delete(PROFILE_ID);
    }
}