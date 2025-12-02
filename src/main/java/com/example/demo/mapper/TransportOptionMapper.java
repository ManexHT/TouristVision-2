package com.example.demo.mapper;

import com.example.demo.dto.TransportOptionRequest;
import com.example.demo.dto.TransportOptionResponse;
import com.example.demo.model.TransportOption;

public final class TransportOptionMapper {

    public static TransportOptionResponse toResponse(TransportOption entity) {
        if (entity == null) return null;
        return TransportOptionResponse.builder()
                .id_transport(entity.getId())
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .type(entity.getType())
                .description(entity.getDescription())
                .build();
    }

    public static TransportOption toEntity(TransportOptionRequest dto) {
        if (dto == null) return null;
        return TransportOption.builder()
                .type(dto.getType())
                .description(dto.getDescription())
                .build();
    }

    public static void copyToEntity(TransportOptionRequest dto, TransportOption entity) {
        if (dto == null || entity == null) return;
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());
    }
}
