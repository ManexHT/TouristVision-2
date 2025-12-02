package com.example.demo.mapper;

import com.example.demo.dto.AppUserRequest;
import com.example.demo.dto.AppUserResponse;
import com.example.demo.model.AppUser;

public final class AppUserMapper {

    public static AppUserResponse toResponse(AppUser entity) {
        if (entity == null) return null;
        return AppUserResponse.builder()
                .id_user(entity.getId())
                .username(entity.getUsername())
                .rolId(entity.getRol() != null ? entity.getRol().getId() : null)
                .build();
    }

    public static AppUser toEntity(AppUserRequest dto) {
        if (dto == null) return null;
        return AppUser.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    public static void copyToEntity(AppUserRequest dto, AppUser entity) {
        if (dto == null || entity == null) return;
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
    }
}

