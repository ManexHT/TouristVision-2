package com.example.demo.mapper;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.model.Review;

public final class ReviewMapper {

    public static ReviewResponse toResponse(Review entity) {
        if (entity == null) return null;
        return ReviewResponse.builder()
                .id_review(entity.getId())
                .id_user(entity.getUser() != null ? entity.getUser().getId() : null)
                .id_place(entity.getPlace() != null ? entity.getPlace().getId() : null)
                .title(entity.getTitle())
                .content(entity.getContent())
                .rating(entity.getRating())
                .build();
    }

    public static Review toEntity(ReviewRequest dto) {
        if (dto == null) return null;
        return Review.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .rating(dto.getRating())
                .build();
    }

    public static void copyToEntity(ReviewRequest dto, Review entity) {
        if (dto == null || entity == null) return;
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setRating(dto.getRating());
    }
}
