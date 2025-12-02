package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MunicipalityRequest {
    
    @NotBlank(message = "Municipality name cannot be blank")
    @Size(max = 100, message = "Municipality name cannot exceed 100 characters")
    private String name;
}