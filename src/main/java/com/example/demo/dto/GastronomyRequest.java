package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GastronomyRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    private String description;
}
