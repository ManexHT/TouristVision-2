package com.example.demo.service;

import com.example.demo.dto.ServiceReviewRequest;
import com.example.demo.dto.ServiceReviewResponse;
import com.example.demo.mapper.ServiceReviewMapper;
import com.example.demo.model.AppUser;
import com.example.demo.model.ServiceReview;
import com.example.demo.model.Services;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.ServiceReviewRepository;
import com.example.demo.repository.ServicesRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceReviewServiceImpl implements ServiceReviewService {

    private final ServiceReviewRepository repository;
    private final AppUserRepository userRepo;
    private final ServicesRepository serviceRepo;

    /*@Override
    public List<ServiceReviewResponse> findAll() {
        return repository.findAll().stream()
                .map(ServiceReviewMapper::toResponse)
                .toList();
    }*/

    @Override
    public List<ServiceReviewResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(ServiceReviewMapper::toResponse)
                .toList();
    }

    @Override
    public ServiceReviewResponse findById(Integer id) {
        ServiceReview review = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + id));
        return ServiceReviewMapper.toResponse(review);
    }

    @Override
    public ServiceReviewResponse create(Integer serviceId, Integer userId, ServiceReviewRequest request) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Services service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found: " + serviceId));

        ServiceReview entity = ServiceReviewMapper.toEntity(request);
        entity.setUser(user);
        entity.setService(service);

        ServiceReview saved = repository.save(entity);

        // Recargar la entidad con relaciones completas
        ServiceReview reloaded = repository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Review not found after save: " + saved.getId()));

        return ServiceReviewMapper.toResponse(reloaded);
    }

    @Override
    public ServiceReviewResponse update(Integer id, ServiceReviewRequest request) {
        ServiceReview existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + id));
        ServiceReviewMapper.copyToEntity(request, existing);
        ServiceReview updated = repository.save(existing);
        return ServiceReviewMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Review not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<ServiceReviewResponse> findByRating(Integer rating) {
        List<ServiceReview> results = repository.findByRating(rating);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No reviews found with rating: " + rating);
        }

        return results.stream()
                .map(ServiceReviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<ServiceReviewResponse> searchByComment(String keyword) {
        List<ServiceReview> results = repository.findByCommentContainingIgnoreCase(keyword);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No reviews found containing keyword: " + keyword);
        }

        return results.stream()
                .map(ServiceReviewMapper::toResponse)
                .toList();
    }

}
