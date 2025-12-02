package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoryResponse {

    private Integer id_story;
    private Integer id_place;
    private String history;
    private String traditions;
}
