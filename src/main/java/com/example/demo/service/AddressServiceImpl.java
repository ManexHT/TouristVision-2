package com.example.demo.service;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AddressResponse findById(Integer id) {
        Address entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + id));
        return AddressMapper.toResponse(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AddressResponse create(AddressRequest request) {
        Address entity = AddressMapper.toEntity(request);
        Address saved = repository.save(entity);
        return AddressMapper.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AddressResponse update(Integer id, AddressRequest request) {
        Address existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + id));
        AddressMapper.copyToEntity(request, existing);
        Address updated = repository.save(existing);
        return AddressMapper.toResponse(updated);
    }

    /* 
    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Address not found: " + id);
        }
        repository.deleteById(id);
    }
    */

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AddressResponse> findByPostalCode(String postalCode) {
        List<Address> results = repository.findByPostalCode(postalCode);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No addresses found with postal code: " + postalCode);
        }
        return results.stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AddressResponse> findByNeighborhoodLike(String neighborhood) {
        List<Address> results = repository.findByNeighborhoodLike(neighborhood);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No addresses found with neighborhood like: " + neighborhood);
        }
        return results.stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AddressResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AddressResponse> findByStreetLike(String street) {
        List<Address> results = repository.findByStreetLike(street);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No addresses found with street like: " + street);
        }
        return results.stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

}
