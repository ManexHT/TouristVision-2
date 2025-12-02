package com.example.demo.repository;

import com.example.demo.model.FavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Integer> {

    // Buscar favoritos por usuario
    @Query(value = "SELECT * FROM favoriteplaces WHERE id_user = :userId", nativeQuery = true)
    List<FavoritePlace> findByUserId(@Param("userId") Integer userId);

    // Validar si un lugar ya está marcado como favorito por el usuario
    @Query(value = "SELECT * FROM favoriteplaces WHERE id_user = :userId AND id_place = :placeId", nativeQuery = true)
    FavoritePlace findByUserIdAndPlaceId(@Param("userId") Integer userId, @Param("placeId") Integer placeId);
}
