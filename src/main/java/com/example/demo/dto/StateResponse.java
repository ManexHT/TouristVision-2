package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Response DTO for state information")
public class StateResponse {

    @Schema(description = "Unique ID of the state", example = "1")
    @JsonProperty("id_state")
    private Integer id_state;

    @Schema(description = "Name of the state", example = "Texas")
    @JsonProperty("name")
    private String name;
}
