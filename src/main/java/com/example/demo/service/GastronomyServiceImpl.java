package com.example.demo.service;

import com.example.demo.dto.GastronomyRequest;
import com.example.demo.dto.GastronomyResponse;
import com.example.demo.mapper.GastronomyMapper;
import com.example.demo.model.Gastronomy;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.GastronomyRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GastronomyServiceImpl implements GastronomyService {

    private final GastronomyRepository repository;
    private final TouristPlaceRepository placeRepo;

    @Override
    public GastronomyResponse findById(Integer id) {
        Gastronomy entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found: " + id));
        return GastronomyMapper.toResponse(entity);
    }

    @Override
    public GastronomyResponse create(Integer placeId, GastronomyRequest request) {
        TouristPlace place = placeRepo.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));

        Gastronomy entity = GastronomyMapper.toEntity(request);
        entity.setPlace(place);

        Gastronomy saved = repository.save(entity);
        return GastronomyMapper.toResponse(saved);
    }

    @Override
    public GastronomyResponse update(Integer id, GastronomyRequest request) {
        Gastronomy existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found: " + id));
        GastronomyMapper.copyToEntity(request, existing);
        Gastronomy updated = repository.save(existing);
        return GastronomyMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Dish not found: " + id);
        }
        repository.deleteById(id);
    }*/

    @Override
    public List<GastronomyResponse> findAllWithPagination(int page, int pageSize) {
        int offset = Math.max(page, 0);
        int limit = Math.max(pageSize, 1);
        return repository.findAllWithPagination(offset, limit).stream()
                .map(GastronomyMapper::toResponse)
                .toList();
    }

    /*@Override
    public List<GastronomyResponse> findAll() {
        return repository.findAll().stream()
                .map(GastronomyMapper::toResponse)
                .toList();
    }*/
}
