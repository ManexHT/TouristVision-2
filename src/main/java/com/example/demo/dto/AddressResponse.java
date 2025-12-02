package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {

    private Integer id_address;
    private String street;
    private String neighborhood;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String reference;
}
