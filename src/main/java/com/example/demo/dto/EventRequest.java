package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class EventRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime eventDate;
}
