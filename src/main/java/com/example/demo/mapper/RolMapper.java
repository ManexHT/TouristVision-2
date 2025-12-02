package com.example.demo.mapper;

import com.example.demo.dto.RolRequest;
import com.example.demo.dto.RolResponse;
import com.example.demo.model.Rol;

public final class RolMapper {

    public static RolResponse toResponse(Rol entity) {
        if (entity == null) return null;
        return RolResponse.builder()
                .id_rol(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static Rol toEntity(RolRequest dto) {
        if (dto == null) return null;
        return Rol.builder()
                .name(dto.getName())
                .build();
    }

    public static void copyToEntity(RolRequest dto, Rol entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
    }
}
