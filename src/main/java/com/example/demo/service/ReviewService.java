package com.example.demo.service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse findById(Integer id);

    ReviewResponse create(Integer userId, Integer placeId, ReviewRequest request);

    ReviewResponse update(Integer id, ReviewRequest request);

    void delete(Integer id);

    List<ReviewResponse> findAllWithPagination(int page, int pageSize);

    //List<ReviewResponse> findByPlaceId(Integer placeId, int page, int pageSize);

    List<ReviewResponse> findByUserId(Integer userId, int page, int pageSize);

    //List<ReviewResponse> findAll();
}
