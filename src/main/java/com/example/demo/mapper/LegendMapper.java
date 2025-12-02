package com.example.demo.mapper;

import com.example.demo.dto.LegendRequest;
import com.example.demo.dto.LegendResponse;
import com.example.demo.model.Legend;

public final class LegendMapper {

    public static LegendResponse toResponse(Legend entity) {
        if (entity == null) return null;
        return LegendResponse.builder()
                .id_legend(entity.getId())
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .title(entity.getTitle())
                .story(entity.getStory())
                .origin(entity.getOrigin())
                .build();
    }

    public static Legend toEntity(LegendRequest dto) {
        if (dto == null) return null;
        return Legend.builder()
                .title(dto.getTitle())
                .story(dto.getStory())
                .origin(dto.getOrigin())
                .build();
    }

    public static void copyToEntity(LegendRequest dto, Legend entity) {
        if (dto == null || entity == null) return;
        entity.setTitle(dto.getTitle());
        entity.setStory(dto.getStory());
        entity.setOrigin(dto.getOrigin());
    }
}
