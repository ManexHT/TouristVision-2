package com.example.demo.service;

import com.example.demo.dto.TransportOptionRequest;
import com.example.demo.dto.TransportOptionResponse;

import java.util.List;

public interface TransportOptionService {

    TransportOptionResponse findById(Integer id);

    TransportOptionResponse create(Integer placeId, TransportOptionRequest request);

    TransportOptionResponse update(Integer id, TransportOptionRequest request);

    //void delete(Integer id);

    //List<TransportOptionResponse> findAll();

    List<TransportOptionResponse> findAllWithPagination(int page, int pageSize);

    List<TransportOptionResponse> findByType(String type);

}

