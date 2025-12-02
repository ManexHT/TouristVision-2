package com.example.demo.service;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse findById(Integer id);

    AddressResponse create(AddressRequest request);

    AddressResponse update(Integer id, AddressRequest request);

    //void delete(Integer id);

    List<AddressResponse> findByPostalCode(String postalCode);

    List<AddressResponse> findByNeighborhoodLike(String neighborhood);

    List<AddressResponse> findByStreetLike(String street);

    List<AddressResponse> findAllWithPagination(int page, int pageSize);
}
