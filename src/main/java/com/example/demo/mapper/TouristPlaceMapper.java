package com.example.demo.mapper;

import com.example.demo.dto.TouristPlaceRequest;
import com.example.demo.dto.TouristPlaceResponse;
import com.example.demo.model.TouristPlace;

public final class TouristPlaceMapper {

    public static TouristPlaceResponse toResponse(TouristPlace entity) {
        if (entity == null) return null;
        return TouristPlaceResponse.builder()
                .id_place(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .municipalityId(entity.getMunicipality() != null ? entity.getMunicipality().getId() : null)
                .addressId(entity.getAddress() != null ? entity.getAddress().getId() : null)
                .build();
    }

    public static TouristPlace toEntity(TouristPlaceRequest dto) {
        if (dto == null) return null;
        return TouristPlace.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static void copyToEntity(TouristPlaceRequest dto, TouristPlace entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
