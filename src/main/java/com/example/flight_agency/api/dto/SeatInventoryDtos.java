package com.example.flight_agency.api.dto;
import com.example.flight_agency.domine.entities.Cabin;

import java.io.Serializable;
public class SeatInventoryDtos {
    public record SeatInventoryResponse(
            Long id,
            Cabin cabin,
            Integer totalSeats,
            Integer availableSeats
    ) implements Serializable {}
    public record SeatInventoryCreateRequest(
            Long flightId,
            Cabin cabin,
            Integer totalSeats,
            Integer availableSeats
    ) implements Serializable {}

    public record SeatInventoryUpdateRequest(
            Integer totalSeats,
            Integer availableSeats
    ) implements Serializable {}
}
