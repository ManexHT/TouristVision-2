package com.example.demo.service;

import com.example.demo.dto.TouristPlaceRequest;
import com.example.demo.dto.TouristPlaceResponse;

import java.util.List;

public interface TouristPlaceService {

    //List<TouristPlaceResponse> findAll();

    TouristPlaceResponse findById(Integer id);

    TouristPlaceResponse create(Integer municipalityId, Integer addressId, TouristPlaceRequest request);

    TouristPlaceResponse update(Integer id, Integer municipalityId, Integer addressId, TouristPlaceRequest request);

    //void delete(Integer id);

    List<TouristPlaceResponse> findByNameLike(String name);

    List<TouristPlaceResponse> findByMunicipalityId(Integer municipalityId);

    List<TouristPlaceResponse> findAllWithPagination(int page, int pageSize);
}
