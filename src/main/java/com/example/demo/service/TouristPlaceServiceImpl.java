package com.example.demo.service;

import com.example.demo.dto.TouristPlaceRequest;
import com.example.demo.dto.TouristPlaceResponse;
import com.example.demo.mapper.TouristPlaceMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Municipality;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.MunicipalityRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristPlaceServiceImpl implements TouristPlaceService {

    private final TouristPlaceRepository repository;
    private final MunicipalityRepository municipalityRepository;
    private final AddressRepository addressRepository;

    /*@Override
    public List<TouristPlaceResponse> findAll() {
        return repository.findAll().stream()
                .map(TouristPlaceMapper::toResponse)
                .toList();
    }*/

    @Override
    public TouristPlaceResponse findById(Integer id) {
        TouristPlace entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tourist place not found: " + id));
        return TouristPlaceMapper.toResponse(entity);
    }

    @Override
    public TouristPlaceResponse create(Integer municipalityId, Integer addressId, TouristPlaceRequest request) {
        TouristPlace entity = TouristPlaceMapper.toEntity(request);

        Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new EntityNotFoundException("Municipality not found: " + municipalityId));
        entity.setMunicipality(municipality);

        if (addressId != null) {
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));
            entity.setAddress(address);
        }

        TouristPlace saved = repository.save(entity);
        return TouristPlaceMapper.toResponse(saved);
    }

    @Override
    public TouristPlaceResponse update(Integer id, Integer municipalityId, Integer addressId,
            TouristPlaceRequest request) {
        TouristPlace existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tourist place not found: " + id));

        TouristPlaceMapper.copyToEntity(request, existing);

        Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new EntityNotFoundException("Municipality not found: " + municipalityId));
        existing.setMunicipality(municipality);

        if (addressId != null) {
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));
            existing.setAddress(address);
        }

        TouristPlace updated = repository.save(existing);
        return TouristPlaceMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tourist place not found: " + id);
        }
        repository.deleteById(id);
    }*/

    @Override
    public List<TouristPlaceResponse> findByNameLike(String name) {
        return repository.findByNameLike(name).stream()
                .map(TouristPlaceMapper::toResponse)
                .toList();
    }

    @Override
    public List<TouristPlaceResponse> findByMunicipalityId(Integer municipalityId) {
        return repository.findByMunicipalityId(municipalityId).stream()
                .map(TouristPlaceMapper::toResponse)
                .toList();
    }

    @Override
    public List<TouristPlaceResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(TouristPlaceMapper::toResponse)
                .toList();
    }

}
