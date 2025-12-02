package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewImageRequest {

    @NotBlank
    private String url;

    private String description;
}
