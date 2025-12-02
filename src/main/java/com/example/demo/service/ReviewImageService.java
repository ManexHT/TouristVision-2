package com.example.demo.service;

import com.example.demo.dto.ReviewImageRequest;
import com.example.demo.dto.ReviewImageResponse;

import java.util.List;

public interface ReviewImageService {

    ReviewImageResponse findById(Integer id);

    ReviewImageResponse create(Integer reviewId, ReviewImageRequest request);

    ReviewImageResponse update(Integer id, ReviewImageRequest request);

    void delete(Integer id);

    List<ReviewImageResponse> findByReviewId(Integer reviewId);

    //List<ReviewImageResponse> findAll();

    List<ReviewImageResponse> findAllWithPagination(int page, int pageSize);
}
