package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.domine.entities.Booking;
import com.example.flight_agency.domine.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDtos.BookingResponse toResponse(Booking booking);

    Booking toEntity(BookingDtos.BookingCreateRequest request, Passenger passenger);
}
