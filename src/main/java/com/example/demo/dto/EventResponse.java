package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {

    private Integer id_event;
    private Integer id_place;
    private String name;
    private String description;
    private LocalDateTime eventDate;
}
