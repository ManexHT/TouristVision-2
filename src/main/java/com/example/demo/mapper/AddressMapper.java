package com.example.demo.mapper;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.model.Address;

public final class AddressMapper {

    public static AddressResponse toResponse(Address entity) {
        if (entity == null) return null;
        return AddressResponse.builder()
                .id_address(entity.getId())
                .street(entity.getStreet())
                .neighborhood(entity.getNeighborhood())
                .postalCode(entity.getPostalCode())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .reference(entity.getReference())
                .build();
    }

    public static Address toEntity(AddressRequest dto) {
        if (dto == null) return null;
        return Address.builder()
                .street(dto.getStreet())
                .neighborhood(dto.getNeighborhood())
                .postalCode(dto.getPostalCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .reference(dto.getReference())
                .build();
    }

    public static void copyToEntity(AddressRequest dto, Address entity) {
        if (dto == null || entity == null) return;
        entity.setStreet(dto.getStreet());
        entity.setNeighborhood(dto.getNeighborhood());
        entity.setPostalCode(dto.getPostalCode());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setReference(dto.getReference());
    }
}
