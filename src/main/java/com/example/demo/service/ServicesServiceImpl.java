package com.example.demo.service;

import com.example.demo.dto.ServicesRequest;
import com.example.demo.dto.ServicesResponse;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Services;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.ServicesRepository;
import com.example.demo.repository.TouristPlaceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository repository;

    private final TouristPlaceRepository touristPlaceRepository;

    private final AddressRepository addressRepository;

    @Override
    public List<ServicesResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(ServicesMapper::toResponse)
                .toList();
    }

    @Override
    public ServicesResponse findById(Integer id) {
        Services entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found: " + id));
        return ServicesMapper.toResponse(entity);
    }

    @Override
    public ServicesResponse create(Integer placeId, Integer addressId, ServicesRequest request) {
        Services entity = ServicesMapper.toEntity(request);

        TouristPlace place = touristPlaceRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));
        entity.setPlace(place);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));
        entity.setAddress(address);

        Services saved = repository.save(entity);
        return ServicesMapper.toResponse(saved);
    }

    @Override
    public ServicesResponse update(Integer id, Integer placeId, Integer addressId, ServicesRequest request) {
        Services existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found: " + id));
        ServicesMapper.copyToEntity(request, existing);

        // Actualizar campos básicos
        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setDescription(request.getDescription());
        existing.setPriceRange(request.getPriceRange());
        existing.setContactInfo(request.getContactInfo());

        // Actualizar lugar turístico
        TouristPlace place = touristPlaceRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));
        existing.setPlace(place);

        // Actualizar dirección
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));
        existing.setAddress(address);

        Services updated = repository.save(existing);
        return ServicesMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Service not found: " + id);
        }
        repository.deleteById(id);
    }*/

    @Override
    public List<ServicesResponse> findByNameLike(String name) {
        List<Services> results = repository.findByNameLike(name);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No services found with name like: " + name);
        }
        return results.stream()
                .map(ServicesMapper::toResponse)
                .toList();
    }

    @Override
    public List<ServicesResponse> findByType(String type) {
        List<Services> results = repository.findByType(type);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No services found with type: " + type);
        }
        return results.stream()
                .map(ServicesMapper::toResponse)
                .toList();
    }

    @Override
    public List<ServicesResponse> findByPriceRange(String range) {
        List<Services> results = repository.findByPriceRange(range);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No services found with price range containing: " + range);
        }
        return results.stream()
                .map(ServicesMapper::toResponse)
                .toList();
    }

    @Override
    public List<ServicesResponse> findByTouristPlaceName(String placeName) {
        List<Services> results = repository.findByTouristPlaceName(placeName);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No services found for tourist place: " + placeName);
        }
        return results.stream()
                .map(ServicesMapper::toResponse)
                .toList();
    }

}
