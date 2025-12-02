package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LegendResponse {

    private Integer id_legend;
    private Integer id_place;
    private String title;
    private String story;
    private String origin;
}
