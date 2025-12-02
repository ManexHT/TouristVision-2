package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceReviewRequest {

    @NotNull
    private Integer rating;

    @NotBlank
    @Size (max = 500)
    @Schema(description = "comment on the service review", example = "Muy buen servicio")
    private String comment;
}
