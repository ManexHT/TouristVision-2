package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolResponse {

    private Integer id_rol;
    private String name;
}
