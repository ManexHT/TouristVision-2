package com.example.demo.service;

import com.example.demo.dto.StateRequest;
import com.example.demo.dto.StateResponse;

import java.util.List;

public interface StateService {

    //List<StateResponse> findAll();

    List<StateResponse> findAllWithPagination(int page, int pageSize);

    StateResponse findById(Integer id);

    StateResponse create(StateRequest request);

    StateResponse update(Integer id, StateRequest request);

    List<StateResponse> findByNameLike(String name);
    
}

    //void delete(Integer id);