package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank
    private String street;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String postalCode;

    private Double latitude;

    private Double longitude;

    @Size(max = 500)
    private String reference;
}
