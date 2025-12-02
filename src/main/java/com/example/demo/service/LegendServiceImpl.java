package com.example.demo.service;

import com.example.demo.dto.LegendRequest;
import com.example.demo.dto.LegendResponse;
import com.example.demo.mapper.LegendMapper;
import com.example.demo.model.Legend;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.LegendRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegendServiceImpl implements LegendService {

    private final LegendRepository repository;
    private final TouristPlaceRepository placeRepo;

    @Override
    public LegendResponse findById(Integer id) {
        Legend entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Legend not found: " + id));
        return LegendMapper.toResponse(entity);
    }

    @Override
    public LegendResponse create(Integer placeId, LegendRequest request) {
        TouristPlace place = placeRepo.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));

        Legend entity = LegendMapper.toEntity(request);
        entity.setPlace(place);

        Legend saved = repository.save(entity);
        return LegendMapper.toResponse(saved);
    }

    @Override
    public LegendResponse update(Integer id, LegendRequest request) {
        Legend existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Legend not found: " + id));
        LegendMapper.copyToEntity(request, existing);
        Legend updated = repository.save(existing);
        return LegendMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Legend not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<LegendResponse> findAll() {
        return repository.findAll().stream()
                .map(LegendMapper::toResponse)
                .toList();
    }*/

    @Override
    public List<LegendResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(LegendMapper::toResponse)
                .toList();
    }
}
