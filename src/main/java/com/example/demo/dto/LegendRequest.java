package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LegendRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String story;

    @Size(max = 150)
    private String origin;
}
