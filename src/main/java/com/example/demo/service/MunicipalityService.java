package com.example.demo.service;

import com.example.demo.dto.MunicipalityRequest;
import com.example.demo.dto.MunicipalityResponse;

import java.util.List;


public interface MunicipalityService {

    //List<MunicipalityResponse> findAll();

    List<MunicipalityResponse> findAllWithPagination(int page, int pageSize);

    MunicipalityResponse findById(Integer id);

    MunicipalityResponse create(Integer stateId, MunicipalityRequest request);

    MunicipalityResponse update(Integer id, Integer stateId, MunicipalityRequest request);

    //void delete(Integer id);

    List<MunicipalityResponse> findByStateId(Integer stateId);

    List<MunicipalityResponse> findByNameLike(String name);

}