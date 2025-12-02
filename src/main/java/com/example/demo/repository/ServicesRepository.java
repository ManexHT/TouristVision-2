package com.example.demo.repository;

import com.example.demo.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Services, Integer> {

    // Consulta especializada: buscar servicios por nombre parcial
    // (case-insensitive)
    @Query(value = "SELECT * FROM services WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Services> findByNameLike(@Param("name") String name);

    // Consulta especializada: buscar servicios por tipo exacto
    @Query(value = "SELECT * FROM services WHERE type = :type", nativeQuery = true)
    List<Services> findByType(@Param("type") String type);

    // Consulta especializada: buscar servicios por rango de precio
    @Query(value = "SELECT * FROM services WHERE priceRange ILIKE CONCAT('%', :range, '%')", nativeQuery = true)
    List<Services> findByPriceRange(@Param("range") String range);

    // Buscar todos los servicios por nombre parcial del lugar turístico
    @Query(value = """
                SELECT s.* FROM services s
                JOIN touristplaces tp ON s.id_place = tp.id_place
                WHERE tp.name ILIKE CONCAT('%', :placeName, '%')
            """, nativeQuery = true)
    List<Services> findByTouristPlaceName(@Param("placeName") String placeName);

    // Consulta especializada: paginación
    @Query(value = "SELECT * FROM services ORDER BY id_service LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<Services> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}
