package com.example.demo.repository;

import com.example.demo.model.ServiceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceReviewRepository extends JpaRepository<ServiceReview, Integer> {

    @Query(value = "SELECT * FROM servicereviews WHERE rating = :rating", nativeQuery = true)
    List<ServiceReview> findByRating(@Param("rating") Integer rating);

    @Query(value = "SELECT * FROM servicereviews WHERE comment ILIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<ServiceReview> findByCommentContainingIgnoreCase(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM servicereviews WHERE rating >= :minRating", nativeQuery = true)
    List<ServiceReview> findByRatingGreaterThanEqual(@Param("minRating") Integer minRating);

    @Query(value = "SELECT * FROM servicereviews ORDER BY id_serviceReview LIMIT :pageSize OFFSET GREATEST((:page - 1) * :pageSize, 0)", nativeQuery = true)
    List<ServiceReview> findAllWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);

}
