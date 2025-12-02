package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserResponse {

    private Integer id_user;
    private String username;
    private Integer rolId;
}
