package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.api.error.NotFoundException; // Asumo que usas esta excepción para 404
import com.example.flight_agency.services.TagService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TagService service;
    private final String BASE_URL = "/api/v1/tags";
    private final Long TAG_ID = 5L;
    private final String TAG_NAME = "PROMOTION";
    private final TagDtos.TagCreateRequest createRequest =
            new TagDtos.TagCreateRequest(TAG_NAME);
    private final TagDtos.TagResponse responseDTO =
            new TagDtos.TagResponse(TAG_ID, TAG_NAME);


    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST /tags - Debe crear una etiqueta y retornar 201 CREATED")
    void create_should() throws Exception {
        when(service.create(any(TagDtos.TagCreateRequest.class))).thenReturn(responseDTO);
        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TAG_ID))
                .andExpect(jsonPath("$.name").value(TAG_NAME));

        verify(service).create(any(TagDtos.TagCreateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /tags/{id} - Debe retornar una etiqueta por ID y 200 OK")
    void findById_should() throws Exception {
        when(service.findById(TAG_ID)).thenReturn(responseDTO);
        mvc.perform(get(BASE_URL + "/{id}", TAG_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TAG_ID))
                .andExpect(jsonPath("$.name").value(TAG_NAME));

        verify(service).findById(TAG_ID);
    }

    @Test
    @DisplayName("GET /tags/{id} - Debe retornar 404 NOT FOUND si el ID no existe")
    void findById_shouldReturn404WhenNotFound() throws Exception {
        final Long NON_EXISTENT_ID = 99L;
        final String ERROR_MESSAGE = "Tag " + NON_EXISTENT_ID + " not found";

        doThrow(new NotFoundException(ERROR_MESSAGE)).when(service).findById(NON_EXISTENT_ID);
        mvc.perform(get(BASE_URL + "/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(service).findById(NON_EXISTENT_ID);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /tags/by-name/{name} - Debe retornar una etiqueta por nombre y 200 OK")
    void findByName() throws Exception {
        when(service.findByName(TAG_NAME)).thenReturn(responseDTO);
        mvc.perform(get(BASE_URL + "/by-name/{name}", TAG_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TAG_NAME))
                .andExpect(jsonPath("$.id").value(TAG_ID));

        verify(service).findByName(TAG_NAME);
    }

    @Test
    @DisplayName("GET /tags/by-name/{name} - Debe retornar 404 NOT FOUND si el nombre no existe")
    void findByName_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        final String NON_EXISTENT_NAME = "NON_EXISTENT";
        final String ERROR_MESSAGE = "Tag " + NON_EXISTENT_NAME + " not found";

        doThrow(new NotFoundException(ERROR_MESSAGE)).when(service).findByName(NON_EXISTENT_NAME);
        mvc.perform(get(BASE_URL + "/by-name/{name}", NON_EXISTENT_NAME))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(service).findByName(NON_EXISTENT_NAME);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /tags - Debe retornar la lista de todas las etiquetas y 200 OK")
    void findAll() throws Exception {
        // Arrange
        final TagDtos.TagResponse otherTag = new TagDtos.TagResponse(6L, "DISCOUNT");
        List<TagDtos.TagResponse> tagList = List.of(responseDTO, otherTag);

        when(service.findAll()).thenReturn(tagList);

        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(TAG_NAME))
                .andExpect(jsonPath("$[1].name").value("DISCOUNT"));

        verify(service).findAll();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("DELETE /tags/{id} - Debe eliminar una etiqueta y retornar 204 NO CONTENT")
    void delete_should() throws Exception {
        doNothing().when(service).delete(TAG_ID);
        mvc.perform(delete(BASE_URL + "/{id}", TAG_ID))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service).delete(TAG_ID);
    }
}