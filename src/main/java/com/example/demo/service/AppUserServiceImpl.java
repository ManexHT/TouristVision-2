package com.example.demo.service;

import com.example.demo.dto.AppUserRequest;
import com.example.demo.dto.AppUserResponse;
import com.example.demo.mapper.AppUserMapper;
import com.example.demo.model.AppUser;
import com.example.demo.model.Rol;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    private final RolRepository rolRepository;

    @Override
    public AppUserResponse findById(Integer id) {
        AppUser entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return AppUserMapper.toResponse(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AppUserResponse create(Integer rolId, AppUserRequest request) {
        AppUser entity = AppUserMapper.toEntity(request);

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new EntityNotFoundException("Rol not found: " + rolId));
        entity.setRol(rol);

        AppUser saved = repository.save(entity);
        return AppUserMapper.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public AppUserResponse update(Integer id, Integer rolId, AppUserRequest request) {
        AppUser existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        AppUserMapper.copyToEntity(request, existing);

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new EntityNotFoundException("Rol not found: " + rolId));
        existing.setRol(rol);

        AppUser updated = repository.save(existing);
        return AppUserMapper.toResponse(updated);
    }

    /*@Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        repository.deleteById(id);
    }
    */

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AppUserResponse> findByUsernameLike(String username) {
        List<AppUser> results = repository.findByUsernameLike(username);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No users found with username like: " + username);
        }
        return results.stream()
                .map(AppUserMapper::toResponse)
                .toList();
    }

    /*@Override
    public List<AppUserResponse> findAll() {
        return repository.findAll().stream()
                .map(AppUserMapper::toResponse)
                .toList();
    }
    */

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<AppUserResponse> findAllWithPagination(int page, int pageSize, int rolId) {
        List<AppUser> results = repository.findAllWithPagination(page, pageSize);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("No users found for the requested page");
        }
        return results.stream()
                .map(AppUserMapper::toResponse)
                .toList();
    }
}