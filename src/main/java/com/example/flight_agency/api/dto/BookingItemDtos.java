package com.example.flight_agency.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
public class BookingItemDtos {
    public record BookingItemResponse(Long id, String cabin, BigDecimal price, Integer segmentOrder, FlightDtos.FlightResponseBasic flight) implements Serializable {}
}
