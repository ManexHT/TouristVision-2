package com.example.demo.repository;

import com.example.demo.model.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Integer> {

    // Buscar lugares turísticos por nombre parcial (case-insensitive)
    @Query(value = "SELECT * FROM touristplaces WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<TouristPlace> findByNameLike(@Param("name") String name);

    // Buscar lugares turísticos por municipio
    @Query(value = "SELECT * FROM touristplaces WHERE id_municipality = :municipalityId", nativeQuery = true)
    List<TouristPlace> findByMunicipalityId(@Param("municipalityId") Integer municipalityId);

    // Paginación
    @Query(value = "SELECT * FROM touristplaces ORDER BY id_place LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<TouristPlace> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}
