package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.domine.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagDtos.TagResponse toResponse(Tag tag);

    Tag toEntity(TagDtos.TagCreateRequest request);
}
