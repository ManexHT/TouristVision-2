package com.example.demo.service;

import com.example.demo.dto.StoryRequest;
import com.example.demo.dto.StoryResponse;
import com.example.demo.mapper.StoryMapper;
import com.example.demo.model.Story;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.StoryRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository repository;
    private final TouristPlaceRepository placeRepo;

    @Override
    public StoryResponse findById(Integer id) {
        Story entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Story not found: " + id));
        return StoryMapper.toResponse(entity);
    }

    @Override
    public StoryResponse create(Integer placeId, StoryRequest request) {
        TouristPlace place = placeRepo.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));

        Story entity = StoryMapper.toEntity(request);
        entity.setPlace(place);

        Story saved = repository.save(entity);
        return StoryMapper.toResponse(saved);
    }

    /*@Override
    public StoryResponse update(Integer id, StoryRequest request) {
        Story existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Story not found: " + id));
        StoryMapper.copyToEntity(request, existing);
        Story updated = repository.save(existing);
        return StoryMapper.toResponse(updated);
    }*/

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Story not found: " + id);
        }
        repository.deleteById(id);
    } */

    /*@Override
    public List<StoryResponse> findAll() {
        return repository.findAll().stream()
                .map(StoryMapper::toResponse)
                .toList();
    }*/

    @Override
    public List<StoryResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(StoryMapper::toResponse)
                .toList();
    }
}
