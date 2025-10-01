package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.TagDtos;

import java.util.List;

public interface TagService {

    TagDtos.TagResponse create(TagDtos.TagCreateRequest request);
    TagDtos.TagResponse findById(Long id);
    TagDtos.TagResponse findByName(String name);
    List<TagDtos.TagResponse> findAll();
    void delete(Long id);
}
