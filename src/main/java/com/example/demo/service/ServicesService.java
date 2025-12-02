package com.example.demo.service;

import com.example.demo.dto.ServicesRequest;
import com.example.demo.dto.ServicesResponse;

import java.util.List;

public interface ServicesService {

    List<ServicesResponse> findAllWithPagination(int page, int pageSize);

    ServicesResponse findById(Integer id);

    ServicesResponse create(Integer placeId, Integer addressId, ServicesRequest request);

    ServicesResponse update(Integer id, Integer placeId, Integer addressId, ServicesRequest request);

    //void delete(Integer id);

    List<ServicesResponse> findByNameLike(String name);

    List<ServicesResponse> findByType(String type);

    List<ServicesResponse> findByPriceRange(String range);

    List<ServicesResponse> findByTouristPlaceName(String placeName);

}
