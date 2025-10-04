package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.domine.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  BookingMapper {

    BookingDtos.BookingResponse toResponse(Booking booking);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "items", ignore = true)
    Booking toEntity(BookingDtos.BookingCreateRequest request);
}
