package com.example.demo.mapper;

import com.example.demo.dto.ServiceReviewRequest;
import com.example.demo.dto.ServiceReviewResponse;
import com.example.demo.model.ServiceReview;

public final class ServiceReviewMapper {

    public static ServiceReviewResponse toResponse(ServiceReview entity) {
        if (entity == null)
            return null;
        return ServiceReviewResponse.builder()
                .id_serviceReview(entity.getId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .serviceId(entity.getService() != null ? entity.getService().getId() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .build();
    }

    public static ServiceReview toEntity(ServiceReviewRequest dto) {
        if (dto == null)
            return null;
        return ServiceReview.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }

    public static void copyToEntity(ServiceReviewRequest dto, ServiceReview entity) {
        if (dto == null || entity == null)
            return;
        entity.setRating(dto.getRating());
        entity.setComment(dto.getComment());
    }
}
