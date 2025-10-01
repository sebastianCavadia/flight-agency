package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.domine.entities.Tag;
import com.example.flight_agency.domine.repositories.TagRepository;
import com.example.flight_agency.services.mappers.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {


    private final TagRepository repository;
    private final TagMapper mapper;

    @Override

    public TagDtos.TagResponse create(TagDtos.TagCreateRequest request) {
        Tag tag = mapper.toEntity(request);
        Tag saved = repository.save(tag);
        return mapper.toResponse(saved);
    }

    @Override
    public TagDtos.TagResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Tag no encontrado por id: " + id));
    }

    @Override
    public TagDtos.TagResponse findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Tag no encontrado por nombre: " + name));
    }

    @Override
    public List<TagDtos.TagResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
