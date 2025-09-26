package com.example.flight_agency.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
public class BookingDtos {
    public record BookingCreateRequest(Long passengerId, List<BookingItemCreateRequest> items) implements Serializable {}
    public record BookingItemCreateRequest(Long flightId, String cabin, Integer segmentOrder) implements Serializable {}
    public record BookingResponse(Long id, OffsetDateTime createAt, PassengerDtos.PassengerResponseBasic passenger, List<BookingItemDtos.BookingItemResponse> items) implements Serializable {}
}
