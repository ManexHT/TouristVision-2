package com.example.demo.service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.model.AppUser;
import com.example.demo.model.Review;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final AppUserRepository userRepo;
    private final TouristPlaceRepository placeRepo;

    @Override
    public ReviewResponse findById(Integer id) {
        Review entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + id));
        return ReviewMapper.toResponse(entity);
    }

    @Override
    public ReviewResponse create(Integer userId, Integer placeId, ReviewRequest request) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        TouristPlace place = placeRepo.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));

        Review entity = ReviewMapper.toEntity(request);
        entity.setUser(user);
        entity.setPlace(place);

        Review saved = repository.save(entity);
        return ReviewMapper.toResponse(saved);
    }

    @Override
    public ReviewResponse update(Integer id, ReviewRequest request) {
        Review existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + id));
        ReviewMapper.copyToEntity(request, existing);
        Review updated = repository.save(existing);
        return ReviewMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Review not found: " + id);
        }
        repository.deleteById(id);
    }

    /*
     * @Override
     * public List<ReviewResponse> findByPlaceId(Integer placeId, int page, int
     * pageSize) {
     * if (!placeRepo.existsById(placeId)) {
     * throw new EntityNotFoundException("Place not found: " + placeId);
     * }
     * 
     * return repository.findByPlaceIdWithPagination(placeId, page,
     * pageSize).stream()
     * .map(ReviewMapper::toResponse)
     * .toList();
     * }
     */

    @Override
    public List<ReviewResponse> findByUserId(Integer userId, int page, int pageSize) {
        return repository.findByUserIdWithPagination(userId, page, pageSize).stream()
                .map(ReviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(ReviewMapper::toResponse)
                .toList();
    }

    /*
     * @Override
     * public List<ReviewResponse> findAll() {
     * return repository.findAll().stream()
     * .map(ReviewMapper::toResponse)
     * .toList();
     * }
     */
}
