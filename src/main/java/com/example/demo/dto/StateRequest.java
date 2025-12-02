package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StateRequest {
    
    @NotBlank(message = "State name cannot be blank")
    @Size(max = 100, message = "State name must not exceed 100 characters")
    @Schema(description = "Name of the state", example = "California")
    private String name;
}
