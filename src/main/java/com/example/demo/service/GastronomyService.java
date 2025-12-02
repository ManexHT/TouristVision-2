package com.example.demo.service;

import com.example.demo.dto.GastronomyRequest;
import com.example.demo.dto.GastronomyResponse;

import java.util.List;

public interface GastronomyService {

    GastronomyResponse findById(Integer id);

    GastronomyResponse create(Integer placeId, GastronomyRequest request);

    GastronomyResponse update(Integer id, GastronomyRequest request);

    //void delete(Integer id);

    List<GastronomyResponse> findAllWithPagination(int page, int pageSize);

    //List<GastronomyResponse> findAll();
}
