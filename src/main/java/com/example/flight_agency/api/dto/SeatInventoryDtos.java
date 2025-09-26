package com.example.flight_agency.api.dto;
import java.io.Serializable;
public class SeatInventoryDtos {
    public record SeatInventoryResponse(Long id, String cabin, Integer totalSeats, Integer availableSeats) implements Serializable {}
}
