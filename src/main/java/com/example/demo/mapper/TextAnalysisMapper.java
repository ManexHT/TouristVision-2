/*package com.example.demo.mapper;

import com.example.demo.dto.TextAnalysisRequest;
import com.example.demo.dto.TextAnalysisResponse;
import com.example.demo.model.TextAnalysis;

public final class TextAnalysisMapper {

    public static TextAnalysisResponse toResponse(TextAnalysis entity) {
        if (entity == null) return null;
        return TextAnalysisResponse.builder()
                .id_textAnalysis(entity.getId())
                .id_review(entity.getReview() != null ? entity.getReview().getId() : null)
                .sentiment(entity.getSentiment())
                .keyPhrases(entity.getKeyPhrases())
                .language(entity.getLanguage())
                .build();
    }

    public static TextAnalysis toEntity(TextAnalysisRequest dto) {
        if (dto == null) return null;
        return TextAnalysis.builder()
                .sentiment(dto.getSentiment())
                .keyPhrases(dto.getKeyPhrases())
                .language(dto.getLanguage())
                .build();
    }

    public static void copyToEntity(TextAnalysisRequest dto, TextAnalysis entity) {
        if (dto == null || entity == null) return;
        entity.setSentiment(dto.getSentiment());
        entity.setKeyPhrases(dto.getKeyPhrases());
        entity.setLanguage(dto.getLanguage());
    }
}
*/