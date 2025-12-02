package com.example.demo.service;

import com.example.demo.dto.AppUserRequest;
import com.example.demo.dto.AppUserResponse;

import java.util.List;

public interface AppUserService {

    //List<AppUserResponse> findAll();

    AppUserResponse findById(Integer id);

    AppUserResponse create(Integer rolId, AppUserRequest request);

    AppUserResponse update(Integer id, Integer rolId, AppUserRequest request);

    //void delete(Integer id);

    List<AppUserResponse> findByUsernameLike(String username);

    List<AppUserResponse> findAllWithPagination(int page, int pageSize, int rolId);

}
