package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.BookingItemDtos;
import com.example.flight_agency.domine.entities.BookingItem;
import com.example.flight_agency.domine.entities.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingItemMapper {
    BookingItemMapper INSTANCE = Mappers.getMapper(BookingItemMapper.class);

    BookingItemDtos.BookingItemResponse toResponse(BookingItem item);

    BookingItem toEntity(BookingItemDtos.BookingItemCreateRequest request, Flight flight);
}
