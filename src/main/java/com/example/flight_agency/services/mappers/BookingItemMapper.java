package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.BookingItemDtos;
import com.example.flight_agency.domine.entities.BookingItem;
import com.example.flight_agency.domine.entities.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingItemMapper {

    BookingItemDtos.BookingItemResponse toResponse(BookingItem item);

    @Mapping(target = "id", ignore = true)        // se genera en DB
    @Mapping(target = "booking", ignore = true)
    BookingItem toEntity(BookingItemDtos.BookingItemCreateRequest request, Flight flight);
}
