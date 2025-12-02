package com.example.demo.mapper;

import com.example.demo.dto.FavoritePlaceRequest;
import com.example.demo.dto.FavoritePlaceResponse;
import com.example.demo.model.FavoritePlace;

public final class FavoritePlaceMapper {

    public static FavoritePlaceResponse toResponse(FavoritePlace entity) {
        if (entity == null) return null;
        return FavoritePlaceResponse.builder()
                .id_favorite(entity.getId())
                .id_user(entity.getUser() != null ? entity.getUser().getId() : null)
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .build();
    }

    public static FavoritePlace toEntity(FavoritePlaceRequest dto) {
        return FavoritePlace.builder().build(); 
    }
}
