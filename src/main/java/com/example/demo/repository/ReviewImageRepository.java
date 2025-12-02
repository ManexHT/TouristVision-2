package com.example.demo.repository;

import com.example.demo.model.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {

    //Obtiene todas las imágenes asociadas a una reseña.
    @Query(value = "SELECT * FROM reviewimages WHERE id_review = :reviewId", nativeQuery = true)
    List<ReviewImage> findByReviewId(@Param("reviewId") Integer reviewId);

    //Paginación útil para vistas administrativas.
    @Query(value = "SELECT * FROM reviewimages ORDER BY id_image LIMIT :pageSize OFFSET GREATEST((:page - 1) * :pageSize, 0)", nativeQuery = true)
    List<ReviewImage> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}
