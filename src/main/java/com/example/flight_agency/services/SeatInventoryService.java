package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.SeatInventoryDtos;
import com.example.flight_agency.domine.entities.Cabin;

public interface SeatInventoryService {

    SeatInventoryDtos.SeatInventoryResponse create(SeatInventoryDtos.SeatInventoryCreateRequest req);
    SeatInventoryDtos.SeatInventoryResponse findById(Long id);
    SeatInventoryDtos.SeatInventoryResponse findByFlightAndCabin(Long flightId, Cabin cabin);
    boolean hasMinimumSeatsAvailable(Long flightId, Cabin cabin, int min);
    boolean reserveSeats(Long flightId, Cabin cabin, int count);
    void releaseSeats(Long flightId, Cabin cabin, int count);
    SeatInventoryDtos.SeatInventoryResponse update(Long id, SeatInventoryDtos.SeatInventoryUpdateRequest req);
    void delete(Long id);
}
