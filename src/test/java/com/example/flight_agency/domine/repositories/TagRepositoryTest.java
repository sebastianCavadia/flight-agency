package com.example.flight_agency.domine.repositories;


import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TagRepositoryTest  extends AbstractRepositoryTest {
    @Autowired
    private TagRepository tagRepository;
    @Test
    @DisplayName("Buscar etiqueta por nombre")
    void findByName() {
        tagRepository.save(Tag.builder().name("promo").build());

        Optional<Tag> found = tagRepository.findByName("promo");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("promo");
    }

    @Test
    @DisplayName("Busca una lista de tags")
    void findAllByNameIn() {

        Tag tag1 = Tag.builder().name("promo").build();
        Tag tag2 = Tag.builder().name("eco").build();
        Tag tag3 = Tag.builder().name("red-eye").build();
        tagRepository.saveAll(List.of(tag1,tag2,tag3));

        List<Tag> tags = tagRepository.findAllByNameIn(List.of("promo", "eco"));

        assertThat(tags).hasSize(2);
        assertThat(tags).extracting(Tag::getName).containsExactlyInAnyOrder("promo", "eco");
    }
    @Test
    @DisplayName("Retorna vacío si no encuentra tag por nombre")
    void findByName_NotFound() {
        Optional<Tag> found = tagRepository.findByName("no-existe");
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Retorna lista vacía si no hay coincidencias en findAllByNameIn")
    void findAllByNameIn_NotFound() {
        tagRepository.save(Tag.builder().name("promo").build());
        List<Tag> tags = tagRepository.findAllByNameIn(List.of("xxx", "yyy"));
        assertThat(tags).isEmpty();
    }

}