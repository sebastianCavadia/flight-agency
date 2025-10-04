package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.*;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.services.BookingService;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    // --- Datos de Prueba ---
    private final String BASE_URL = "/api/v1/bookings";
    private final Long BOOKING_ID = 100L;
    private final Long PASSENGER_ID = 50L;
    private final String PASSENGER_EMAIL = "test@example.com";
    private final OffsetDateTime MOCKED_DATE = OffsetDateTime.parse("2025-12-10T10:00:00Z");

    private BookingDtos.BookingCreateRequest createRequest;
    private BookingDtos.BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        PassengerDtos.PassengerResponseBasic passengerBasic = new PassengerDtos.PassengerResponseBasic(
                PASSENGER_ID, "John Doe", PASSENGER_EMAIL);

        FlightDtos.FlightResponseBasic flightBasic = new FlightDtos.FlightResponseBasic(300L, "AA100",OffsetDateTime.now(),OffsetDateTime.now().plusDays(1));

        BookingItemDtos.BookingItemResponse bookingItemResponse = new BookingItemDtos.BookingItemResponse(
                20L, Cabin.BUSINESS, BigDecimal.valueOf(500.75), 1, flightBasic);

        BookingDtos.BookingItemCreateRequest itemCreateRequest = new BookingDtos.BookingItemCreateRequest(
                400L, Cabin.BUSINESS, 1);
        this.createRequest = new BookingDtos.BookingCreateRequest(
                PASSENGER_ID, List.of(itemCreateRequest));

        // RESPONSE DTO complejo: BookingResponse
        this.bookingResponse = new BookingDtos.BookingResponse(
                BOOKING_ID,
                MOCKED_DATE,
                passengerBasic,
                List.of(bookingItemResponse)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("POST /bookings - Debe crear una reserva y retornar 201 Created")
    void createBooking_should() throws Exception {
        when(bookingService.create(any(BookingDtos.BookingCreateRequest.class)))
                .thenReturn(bookingResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOOKING_ID))
                .andExpect(jsonPath("$.passenger.email").value(PASSENGER_EMAIL))
                .andExpect(jsonPath("$.items[0].price").value(500.75));

        verify(bookingService).create(any(BookingDtos.BookingCreateRequest.class));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /bookings/{id} - Debe retornar reserva por ID y status 200 OK")
    void findBookingById_should() throws Exception {
        when(bookingService.findById(BOOKING_ID)).thenReturn(bookingResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", BOOKING_ID)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID))
                .andExpect(jsonPath("$.passenger.id").value(PASSENGER_ID))
                .andExpect(jsonPath("$.items.length()").value(1));

        verify(bookingService).findById(BOOKING_ID);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("GET /bookings?email=... - Debe retornar reservas paginadas por email y 200 OK")
    void findByPassengerEmail_withEmail_should() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingDtos.BookingResponse> responsePage = new PageImpl<>(List.of(bookingResponse), pageable, 1);

        when(bookingService.findByPassengerEmail(eq(PASSENGER_EMAIL), any(Pageable.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get(BASE_URL)
                        .param("email", PASSENGER_EMAIL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(BOOKING_ID))
                .andExpect(jsonPath("$.content[0].passenger.email").value(PASSENGER_EMAIL))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(bookingService).findByPassengerEmail(eq(PASSENGER_EMAIL), any(Pageable.class));
    }

    @Test
    @DisplayName("GET /bookings - Debe retornar una página vacía si no se proporciona email")
    void findByPassengerEmail_withoutEmail_should() throws Exception {
        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(bookingService, never()).findByPassengerEmail(any(), any());
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("DELETE /bookings/{id} - Debe eliminar la reserva y retornar 204 No Content")
    void deleteBooking_should() throws Exception {
        doNothing().when(bookingService).delete(BOOKING_ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", BOOKING_ID))
                .andExpect(status().isNoContent());

        verify(bookingService).delete(BOOKING_ID);
    }
}