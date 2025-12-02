package com.example.demo.service;

import com.example.demo.dto.ServiceReviewRequest;
import com.example.demo.dto.ServiceReviewResponse;

import java.util.List;

public interface ServiceReviewService {

    //List<ServiceReviewResponse> findAll();

    List<ServiceReviewResponse> findAllWithPagination(int page, int pageSize);

    ServiceReviewResponse findById(Integer id);

    ServiceReviewResponse create(Integer serviceId, Integer userId, ServiceReviewRequest request);

    ServiceReviewResponse update(Integer id, ServiceReviewRequest request);

    void delete(Integer id);

    List<ServiceReviewResponse> findByRating(Integer rating);

    List<ServiceReviewResponse> searchByComment(String keyword);
}
