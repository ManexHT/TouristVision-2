package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    //Paginación útil para vistas administrativas.
    @Query(value = "SELECT * FROM reviews ORDER BY id_review LIMIT :pageSize OFFSET GREATEST((:page - 1) * :pageSize, 0)", nativeQuery = true)
    List<Review> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

    //Obtiene todas las reseñas asociadas a un lugar turístico.
    @Query(value = "SELECT * FROM reviews WHERE id_place = :placeId ORDER BY id_review DESC LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<Review> findByPlaceIdWithPagination(@Param("placeId") Integer placeId, @Param("page") int page, @Param("pageSize") int pageSize);

    //Obtiene todas las reseñas escritas por un usuario.
    @Query(value = "SELECT * FROM reviews WHERE id_user = :userId ORDER BY id_review DESC LIMIT :pageSize OFFSET (:page * :pageSize)", nativeQuery = true)
    List<Review> findByUserIdWithPagination(@Param("userId") Integer userId, @Param("page") int page, @Param("pageSize") int pageSize);
}
