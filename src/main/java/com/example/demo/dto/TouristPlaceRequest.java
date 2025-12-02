package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TouristPlaceRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 1000)
    private String description;
}
