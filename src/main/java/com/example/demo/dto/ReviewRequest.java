package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
