package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.domine.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDtos.TagResponse toResponse(Tag tag);

    Tag toEntity(TagDtos.TagCreateRequest request);
}
