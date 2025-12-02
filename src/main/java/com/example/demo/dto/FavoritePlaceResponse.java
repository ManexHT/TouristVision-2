package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoritePlaceResponse {

    private Integer id_favorite;
    private Integer id_user;
    private Integer id_place;
}
