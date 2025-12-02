package com.example.demo.mapper;

import com.example.demo.dto.MunicipalityRequest;
import com.example.demo.dto.MunicipalityResponse;
import com.example.demo.model.Municipality;

public final class MunicipalityMapper {

    public static MunicipalityResponse toResponse(Municipality entity) {
        if (entity == null)
            return null;
        return MunicipalityResponse.builder()
                .id_municipality(entity.getId())
                .name(entity.getName())
                .stateId(entity.getState() != null ? entity.getState().getId() : null)
                .build();
    }

    public static Municipality toEntity(MunicipalityRequest dto) {
        if (dto == null)
            return null;
        return Municipality.builder()
                .name(dto.getName())
                .build(); 
    }

    public static void copyToEntity(MunicipalityRequest dto, Municipality entity) {
        if (dto == null || entity == null)
            return;
        entity.setName(dto.getName());
    }
}
