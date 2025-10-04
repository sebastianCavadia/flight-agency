package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.TagDtos;
import com.example.flight_agency.domine.repositories.TagRepository;
import com.example.flight_agency.domine.entities.Tag;
import com.example.flight_agency.services.mappers.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private TagRepository tagRepository;
    @Mock
    private TagMapper tagMapper;
    @InjectMocks
    private TagServiceImpl tagService;
    private Tag tag;
    private TagDtos.TagCreateRequest createRequest;
    private TagDtos.TagResponse response;

    @BeforeEach
    void setUp() {
        tag = Tag.builder()
                .id(1L)
                .name("eco")
                .build();
        createRequest = new TagDtos.TagCreateRequest("eco");
        response = new TagDtos.TagResponse(1L, "eco");
    }

    @Test
    @DisplayName("Crear tag")
    void createTag() {
        when(tagMapper.toEntity(createRequest)).thenReturn(tag);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        when(tagMapper.toResponse(tag)).thenReturn(response);

        TagDtos.TagResponse result = tagService.create(createRequest);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("eco");
        verify(tagRepository).save(tag);
    }

    @Test
    @DisplayName("Buscar por id")
    void findById_found() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagMapper.toResponse(tag)).thenReturn(response);

        var result = tagService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("eco");
        verify(tagRepository).findById(1L);
    }

    @Test
    @DisplayName("Buscar id de tag que no existe")
    void findById_notFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tagService.findById(1L));
        verify(tagRepository).findById(1L);
    }

    @Test
    @DisplayName("Buscar por nombre")
    void findByName_found() {
        when(tagRepository.findByName("eco")).thenReturn(Optional.of(tag));
        when(tagMapper.toResponse(tag)).thenReturn(response);

        var result = tagService.findByName("eco");

        assertThat(result.name()).isEqualTo("eco");
        verify(tagRepository).findByName("eco");
    }

    @Test
    @DisplayName("Buscar nombre de tag que no existe")
    void findByName_notFound() {
        when(tagRepository.findByName("promo")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tagService.findByName("promo"));
        verify(tagRepository).findByName("promo");
    }


    @Test
    @DisplayName("Buscar todos los tag")
    void findAllTags() {
        when(tagRepository.findAll()).thenReturn(List.of(tag));
        when(tagMapper.toResponse(tag)).thenReturn(response);

        var result = tagService.findAll();

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(TagDtos.TagResponse::name)
                .isEqualTo("eco");
        verify(tagRepository).findAll();
    }

    @Test
    @DisplayName("Eliminar un tag")
    void deleteTag() {
        doNothing().when(tagRepository).deleteById(1L);
        tagService.delete(1L);
        verify(tagRepository).deleteById(1L);
    }
}