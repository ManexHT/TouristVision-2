package com.example.demo.mapper;

import com.example.demo.dto.EventRequest;
import com.example.demo.dto.EventResponse;
import com.example.demo.model.Event;

public final class EventMapper {

    public static EventResponse toResponse(Event entity) {
        if (entity == null) return null;
        return EventResponse.builder()
                .id_event(entity.getId())
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .name(entity.getName())
                .description(entity.getDescription())
                .eventDate(entity.getEventDate())
                .build();
    }

    public static Event toEntity(EventRequest dto) {
        if (dto == null) return null;
        return Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .build();
    }

    public static void copyToEntity(EventRequest dto, Event entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setEventDate(dto.getEventDate());
    }
}
