package com.example.demo.mapper;

import com.example.demo.dto.GastronomyRequest;
import com.example.demo.dto.GastronomyResponse;
import com.example.demo.model.Gastronomy;

public final class GastronomyMapper {

    public static GastronomyResponse toResponse(Gastronomy entity) {
        if (entity == null) return null;
        return GastronomyResponse.builder()
                .id_dish(entity.getId())
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public static Gastronomy toEntity(GastronomyRequest dto) {
        if (dto == null) return null;
        return Gastronomy.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static void copyToEntity(GastronomyRequest dto, Gastronomy entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
