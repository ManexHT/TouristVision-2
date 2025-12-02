package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoryRequest {

    @NotBlank
    private String history;

    @NotBlank
    private String traditions;
}
