package com.example.demo.mapper;

import com.example.demo.dto.ServicesRequest;
import com.example.demo.dto.ServicesResponse;
import com.example.demo.model.Services;

public final class ServicesMapper {

    public static ServicesResponse toResponse(Services entity) {
        if (entity == null) return null;
        return ServicesResponse.builder()
                .id_service(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .description(entity.getDescription())
                .priceRange(entity.getPriceRange())
                .contactInfo(entity.getContactInfo())
                .placeId(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .addressId(entity.getAddress() != null ? entity.getAddress().getId() : null)
                .build();
    }

    public static Services toEntity(ServicesRequest dto) {
        if (dto == null) return null;
        return Services.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .priceRange(dto.getPriceRange())
                .contactInfo(dto.getContactInfo())
                .build();
    }

    public static void copyToEntity(ServicesRequest dto, Services entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());
        entity.setPriceRange(dto.getPriceRange());
        entity.setContactInfo(dto.getContactInfo());
    }
}
