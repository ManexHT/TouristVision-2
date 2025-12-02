/*package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TextAnalysisRequest {

    @NotBlank
    @Size(max = 20)
    private String sentiment;

    @NotBlank
    @JsonProperty("key Phrases")
    private String keyPhrases;

    @NotBlank
    @Size(max = 10)
    private String language;
}
*/