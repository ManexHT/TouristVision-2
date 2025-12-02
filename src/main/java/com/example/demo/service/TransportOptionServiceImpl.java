package com.example.demo.service;

import com.example.demo.dto.TransportOptionRequest;
import com.example.demo.dto.TransportOptionResponse;
import com.example.demo.mapper.TransportOptionMapper;
import com.example.demo.model.TouristPlace;
import com.example.demo.model.TransportOption;
import com.example.demo.repository.TouristPlaceRepository;
import com.example.demo.repository.TransportOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportOptionServiceImpl implements TransportOptionService {

    private final TransportOptionRepository repository;
    private final TouristPlaceRepository placeRepo;

    @Override
    public TransportOptionResponse findById(Integer id) {
        TransportOption entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TransportOption not found: " + id));
        return TransportOptionMapper.toResponse(entity);
    }

    @Override
    public TransportOptionResponse create(Integer placeId, TransportOptionRequest request) {
        TouristPlace place = placeRepo.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));

        TransportOption entity = TransportOptionMapper.toEntity(request);
        entity.setPlace(place);

        TransportOption saved = repository.save(entity);
        return TransportOptionMapper.toResponse(saved);
    }

    @Override
    public TransportOptionResponse update(Integer id, TransportOptionRequest request) {
        TransportOption existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TransportOption not found: " + id));
        TransportOptionMapper.copyToEntity(request, existing);
        TransportOption updated = repository.save(existing);
        return TransportOptionMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("TransportOption not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<TransportOptionResponse> findAll() {
        return repository.findAll().stream()
                .map(TransportOptionMapper::toResponse)
                .toList();
    }*/

    @Override
    public List<TransportOptionResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(TransportOptionMapper::toResponse)
                .toList();
    }

    @Override
    public List<TransportOptionResponse> findByType(String type) {
        return repository.findByType(type).stream()
                .map(TransportOptionMapper::toResponse)
                .toList();
    }

}
