package com.example.demo.service;

import com.example.demo.dto.StoryRequest;
import com.example.demo.dto.StoryResponse;

import java.util.List;

public interface StoryService {

    StoryResponse findById(Integer id);

    StoryResponse create(Integer placeId, StoryRequest request);

    //StoryResponse update(Integer id, StoryRequest request);

    //void delete(Integer id);

    //List<StoryResponse> findAll();

    List<StoryResponse> findAllWithPagination(int page, int pageSize);
}
