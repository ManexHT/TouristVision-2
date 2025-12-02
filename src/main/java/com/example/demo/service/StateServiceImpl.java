package com.example.demo.service;

import com.example.demo.dto.StateRequest;
import com.example.demo.dto.StateResponse;
import com.example.demo.mapper.StateMapper;
import com.example.demo.model.State;
import com.example.demo.repository.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository repository;

    /*@Override
    public List<StateResponse> findAll() {
        return repository.findAll().stream()
                .map(StateMapper::toResponse)
                .toList();
    }*/

    @Override
    public List<StateResponse> findAllWithPagination(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize).stream()
                .map(StateMapper::toResponse)
                .toList();
    }

    @Override
    public StateResponse findById(Integer id) {
        State state = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("State not found: " + id));
        return StateMapper.toResponse(state);
    }

    @Override
    public StateResponse create(StateRequest request) {
        State saved = repository.save(StateMapper.toEntity(request));
        return StateMapper.toResponse(saved);
    }

    @Override
    public StateResponse update(Integer id, StateRequest request) {
        State existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("State not found: " + id));
        StateMapper.copyToEntity(request, existing);
        State updated = repository.save(existing);
        return StateMapper.toResponse(updated);
    }

    @Override
    public List<StateResponse> findByNameLike(String name) {
        return repository.findByNameLike(name).stream()
                .map(StateMapper::toResponse)
                .toList();
    }
    
}


    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("State not found: " + id);
        }
        repository.deleteById(id);
    }*/