package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.domine.entities.Booking;
import com.example.flight_agency.domine.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDtos.BookingResponse toResponse(Booking booking);
}
