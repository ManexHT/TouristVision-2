/*package com.example.demo.repository;

import com.example.demo.model.ImageAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageAnalysisRepository extends JpaRepository<ImageAnalysis, Integer> {

    // Útil para mostrar resultados de IA por imagen en vistas detalladas.
    @Query(value = "SELECT * FROM imageanalysis WHERE id_image = :imageId", nativeQuery = true)
    List<ImageAnalysis> findByImageId(@Param("imageId") Integer imageId);
}*/
