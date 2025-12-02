package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicesResponse {

    private Integer id_service;
    private String name;
    private String type;
    private String description;

    @JsonProperty("price Range")
    private String priceRange;

    @JsonProperty("contact Info")
    private String contactInfo;

    @JsonProperty("place_Id")
    private Integer placeId;

    @JsonProperty("address_Id")
    private Integer addressId;
}
