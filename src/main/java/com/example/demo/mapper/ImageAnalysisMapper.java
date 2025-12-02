/*package com.example.demo.mapper;

import com.example.demo.dto.ImageAnalysisRequest;
import com.example.demo.dto.ImageAnalysisResponse;
import com.example.demo.model.ImageAnalysis;

public final class ImageAnalysisMapper {

    public static ImageAnalysisResponse toResponse(ImageAnalysis entity) {
        if (entity == null) return null;
        return ImageAnalysisResponse.builder()
                .id_imageAnalysis(entity.getId())
                .id_image(entity.getImage() != null ? entity.getImage().getId() : null)
                .confidence(entity.getConfidence())
                .description(entity.getDescription())
                .build();
    }

    public static ImageAnalysis toEntity(ImageAnalysisRequest dto) {
        if (dto == null) return null;
        return ImageAnalysis.builder()
                .confidence(dto.getConfidence())
                .description(dto.getDescription())
                .build();
    }

    public static void copyToEntity(ImageAnalysisRequest dto, ImageAnalysis entity) {
        if (dto == null || entity == null) return;
        entity.setConfidence(dto.getConfidence());
        entity.setDescription(dto.getDescription());
    }
}*/
