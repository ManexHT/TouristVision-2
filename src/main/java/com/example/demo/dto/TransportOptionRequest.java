package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransportOptionRequest {

    @NotBlank
    @Size(max = 100)
    private String type;

    @NotBlank
    private String description;
}

