package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransportOptionResponse {

    private Integer id_transport;
    private Integer id_place;
    private String type;
    private String description;
}
