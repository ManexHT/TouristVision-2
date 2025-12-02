package com.example.demo.service;

import com.example.demo.dto.RolRequest;
import com.example.demo.dto.RolResponse;
import com.example.demo.mapper.RolMapper;
import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository repository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RolResponse findById(Integer id) {
        Rol entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol not found: " + id));
        return RolMapper.toResponse(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<RolResponse> findByNameLike(String name) {
        List<Rol> results = repository.findByNameLike(name);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No roles found with name like: " + name);
        }
        return results.stream()
                .map(RolMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RolResponse create(RolRequest request) {
        Rol entity = RolMapper.toEntity(request);
        Rol saved = repository.save(entity);
        return RolMapper.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RolResponse update(Integer id, RolRequest request) {
        Rol existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol not found: " + id));
        RolMapper.copyToEntity(request, existing);
        Rol updated = repository.save(existing);
        return RolMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Rol not found: " + id);
        }
        repository.deleteById(id);
    }
    */

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<RolResponse> findAll() {
        return repository.findAll().stream()
                .map(RolMapper::toResponse)
                .toList();
    }
}
