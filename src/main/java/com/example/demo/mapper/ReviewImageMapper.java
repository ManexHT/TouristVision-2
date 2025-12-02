package com.example.demo.mapper;

import com.example.demo.dto.ReviewImageRequest;
import com.example.demo.dto.ReviewImageResponse;
import com.example.demo.model.ReviewImage;

public final class ReviewImageMapper {

    public static ReviewImageResponse toResponse(ReviewImage entity) {
        if (entity == null) return null;
        return ReviewImageResponse.builder()
                .id_image(entity.getId())
                .id_review(entity.getReview() != null ? entity.getReview().getId() : null)
                .url(entity.getUrl())
                .description(entity.getDescription())
                .build();
    }

    public static ReviewImage toEntity(ReviewImageRequest dto) {
        if (dto == null) return null;
        return ReviewImage.builder()
                .url(dto.getUrl())
                .description(dto.getDescription())
                .build();
    }

    public static void copyToEntity(ReviewImageRequest dto, ReviewImage entity) {
        if (dto == null || entity == null) return;
        entity.setUrl(dto.getUrl());
        entity.setDescription(dto.getDescription());
    }
}
