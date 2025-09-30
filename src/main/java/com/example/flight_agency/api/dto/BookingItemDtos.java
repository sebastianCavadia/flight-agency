package com.example.flight_agency.api.dto;
import com.example.flight_agency.domine.entities.Cabin;

import java.io.Serializable;
import java.math.BigDecimal;
public class BookingItemDtos {
    public record BookingItemResponse(Long id,
                                      Cabin cabin,
                                      BigDecimal price,
                                      Integer segmentOrder,
                                      FlightDtos.FlightResponseBasic flight) implements Serializable {}
    public record BookingItemCreateRequest(
            Cabin cabin,
            BigDecimal price,
            Integer segmentOrder,
            Long flightId
    ) implements Serializable {}
}
