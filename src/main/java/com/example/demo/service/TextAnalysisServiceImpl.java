/*package com.example.demo.service;

import com.example.demo.dto.TextAnalysisRequest;
import com.example.demo.dto.TextAnalysisResponse;
import com.example.demo.mapper.TextAnalysisMapper;
import com.example.demo.model.Review;
import com.example.demo.model.TextAnalysis;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.TextAnalysisRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TextAnalysisServiceImpl implements TextAnalysisService {

    private final TextAnalysisRepository repository;
    private final ReviewRepository reviewRepo;

    @Override
    public TextAnalysisResponse findById(Integer id) {
        TextAnalysis entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TextAnalysis not found: " + id));
        return TextAnalysisMapper.toResponse(entity);
    }

    @Override
    public TextAnalysisResponse findByReviewId(Integer reviewId) {
        TextAnalysis entity = repository.findByReviewId(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("TextAnalysis for review not found: " + reviewId));
        return TextAnalysisMapper.toResponse(entity);
    }

    @Override
    public TextAnalysisResponse create(Integer reviewId, TextAnalysisRequest request) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        TextAnalysis entity = TextAnalysisMapper.toEntity(request);
        entity.setReview(review);

        TextAnalysis saved = repository.save(entity);
        return TextAnalysisMapper.toResponse(saved);
    }

    @Override
    public TextAnalysisResponse update(Integer id, TextAnalysisRequest request) {
        TextAnalysis existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TextAnalysis not found: " + id));
        TextAnalysisMapper.copyToEntity(request, existing);
        TextAnalysis updated = repository.save(existing);
        return TextAnalysisMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("TextAnalysis not found: " + id);
        }
        repository.deleteById(id);
    @Override
    public List<TextAnalysisResponse> findAll() {
        return repository.findAll().stream()
                .map(TextAnalysisMapper::toResponse)
                .toList();
    }
}
*/