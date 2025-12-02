package com.example.demo.service;

import com.example.demo.dto.FavoritePlaceRequest;
import com.example.demo.dto.FavoritePlaceResponse;

import java.util.List;

public interface FavoritePlaceService {

    FavoritePlaceResponse findById(Integer id);

    FavoritePlaceResponse create(FavoritePlaceRequest request);

    void delete(Integer id);

    List<FavoritePlaceResponse> findByUserId(Integer userId);

    boolean isFavorite(Integer userId, Integer placeId);
}
