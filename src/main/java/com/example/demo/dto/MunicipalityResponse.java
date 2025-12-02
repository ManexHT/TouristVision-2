package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MunicipalityResponse {

    @JsonProperty("id_municipality")
    private Integer id_municipality;

    @JsonProperty("stateId")
    private Integer stateId;

    @JsonProperty("name")
    private String name;
    
}