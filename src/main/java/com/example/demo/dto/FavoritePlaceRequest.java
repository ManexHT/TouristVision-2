package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoritePlaceRequest {

    @NotNull
    private Integer idUser;

    @NotNull
    private Integer idPlace;
}

