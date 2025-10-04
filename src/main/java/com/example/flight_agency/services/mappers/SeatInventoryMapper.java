package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.SeatInventoryDtos;
import com.example.flight_agency.domine.entities.SeatInventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatInventoryMapper {
    SeatInventoryDtos.SeatInventoryResponse toResponse(SeatInventory seatInventory);
}
