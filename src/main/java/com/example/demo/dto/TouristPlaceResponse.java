package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TouristPlaceResponse {

    private Integer id_place;
    private String name;
    private String description;

    @JsonProperty("municipalityId")
    private Integer municipalityId;

    @JsonProperty("addressId")
    private Integer addressId;
}
