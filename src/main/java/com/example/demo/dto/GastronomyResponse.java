package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GastronomyResponse {

    private Integer id_dish;
    private Integer id_place;
    private String name;
    private String description;
}
