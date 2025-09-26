package com.example.flight_agency.api.dto;
import java.io.Serializable;
public class TagDtos {
    public record TagCreateRequest(String name) implements Serializable {}
    public record TagResponse(Long id, String name) implements Serializable {}
}
