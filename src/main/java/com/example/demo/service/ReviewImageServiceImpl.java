package com.example.demo.service;

import com.example.demo.dto.ReviewImageRequest;
import com.example.demo.dto.ReviewImageResponse;
import com.example.demo.mapper.ReviewImageMapper;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewImage;
import com.example.demo.repository.ReviewImageRepository;
import com.example.demo.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    private final ReviewImageRepository repository;
    private final ReviewRepository reviewRepo;

    @Override
    public ReviewImageResponse findById(Integer id) {
        ReviewImage entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + id));
        return ReviewImageMapper.toResponse(entity);
    }

    @Override
    public ReviewImageResponse create(Integer reviewId, ReviewImageRequest request) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        ReviewImage entity = ReviewImageMapper.toEntity(request);
        entity.setReview(review);

        ReviewImage saved = repository.save(entity);
        return ReviewImageMapper.toResponse(saved);
    }

    @Override
    public ReviewImageResponse update(Integer id, ReviewImageRequest request) {
        ReviewImage existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + id));
        ReviewImageMapper.copyToEntity(request, existing);
        ReviewImage updated = repository.save(existing);
        return ReviewImageMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Image not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<ReviewImageResponse> findByReviewId(Integer reviewId) {
        if (!reviewRepo.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found: " + reviewId);
        }

        return repository.findByReviewId(reviewId).stream()
                .map(ReviewImageMapper::toResponse)
                .toList();
    }

    /*@Override
    public List<ReviewImageResponse> findAll() {
        return repository.findAll().stream()
                .map(ReviewImageMapper::toResponse)
                .toList();
    }*/

    @Override
    public List<ReviewImageResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(ReviewImageMapper::toResponse)
                .toList();
    }
}
