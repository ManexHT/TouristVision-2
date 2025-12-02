package com.example.demo.mapper;

import com.example.demo.dto.StoryRequest;
import com.example.demo.dto.StoryResponse;
import com.example.demo.model.Story;

public final class StoryMapper {

    public static StoryResponse toResponse(Story entity) {
        if (entity == null) return null;
        return StoryResponse.builder()
                .id_story(entity.getId())
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .history(entity.getHistory())
                .traditions(entity.getTraditions())
                .build();
    }

    public static Story toEntity(StoryRequest dto) {
        if (dto == null) return null;
        return Story.builder()
                .history(dto.getHistory())
                .traditions(dto.getTraditions())
                .build();
    }

    public static void copyToEntity(StoryRequest dto, Story entity) {
        if (dto == null || entity == null) return;
        entity.setHistory(dto.getHistory());
        entity.setTraditions(dto.getTraditions());
    }
}
