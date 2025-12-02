package com.example.demo.service;

import com.example.demo.dto.LegendRequest;
import com.example.demo.dto.LegendResponse;

import java.util.List;

public interface LegendService {

    LegendResponse findById(Integer id);

    LegendResponse create(Integer placeId, LegendRequest request);

    LegendResponse update(Integer id, LegendRequest request);

    //void delete(Integer id);

    //List<LegendResponse> findAll();

    List<LegendResponse> findAllWithPagination(int page, int pageSize);
}
