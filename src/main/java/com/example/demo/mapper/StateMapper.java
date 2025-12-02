package com.example.demo.mapper;

import com.example.demo.dto.StateRequest;
import com.example.demo.dto.StateResponse;
import com.example.demo.model.State;

public final class StateMapper {

    public static StateResponse toResponse(State entity) {
        if (entity == null)
            return null;
        return StateResponse.builder()
                .id_state(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static State toEntity(StateRequest dto) {
        if (dto == null)
            return null;
        return State.builder()
                .name(dto.getName())
                .build();
    }

    public static void copyToEntity(StateRequest dto, State entity) {
        if (dto == null || entity == null)
            return;
        entity.setName(dto.getName());
    }
}
