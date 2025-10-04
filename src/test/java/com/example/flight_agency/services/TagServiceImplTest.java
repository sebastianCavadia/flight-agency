package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.domine.repositories.TagRepository;
import com.example.flight_agency.domine.entities.Tag;
import com.example.flight_agency.services.mappers.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class TagServiceImplTest {

    @Mock
    private TagRepository repository;

    @Mock
    private TagMapper mapper;

    @InjectMocks
    private TagServiceImpl service;

    private Tag tag;
    private TagDtos.TagCreateRequest createRequest;
    private TagDtos.TagResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tag = Tag.builder().id(1L).name("eco").build();
        createRequest = new TagDtos.TagCreateRequest("eco");
        response = new TagDtos.TagResponse(1L, "eco");
    }

    @Test
    void createTag() {
        when(mapper.toEntity(createRequest)).thenReturn(tag);
        when(repository.save(any(Tag.class))).thenReturn(tag);
        when(mapper.toResponse(tag)).thenReturn(response);

        TagDtos.TagResponse result = service.create(createRequest);

        assertThat(result.name()).isEqualTo("eco");
        verify(repository).save(tag);
    }

    @Test
    void findById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(tag));
        when(mapper.toResponse(tag)).thenReturn(response);

        var result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    void findById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1L));
    }

    @Test
    void findByName_found() {
        when(repository.findByName("eco")).thenReturn(Optional.of(tag));
        when(mapper.toResponse(tag)).thenReturn(response);

        var result = service.findByName("eco");

        assertThat(result.name()).isEqualTo("eco");
        verify(repository).findByName("eco");
    }

    @Test
    void findByName_notFound() {
        when(repository.findByName("promo")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findByName("promo"));
    }

    @Test
    void findAllTags() {
        when(repository.findAll()).thenReturn(List.of(tag));
        when(mapper.toResponse(tag)).thenReturn(response);

        var result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("eco");
    }

    @Test
    void deleteTag() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}