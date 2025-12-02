package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServicesRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String type;

    private String description;

    @Size(max = 50)
    private String priceRange;

    @Size(max = 150)
    private String contactInfo;
}
