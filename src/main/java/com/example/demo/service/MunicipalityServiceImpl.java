package com.example.demo.service;

import com.example.demo.dto.MunicipalityRequest;
import com.example.demo.dto.MunicipalityResponse;
import com.example.demo.mapper.MunicipalityMapper;
import com.example.demo.model.Municipality;
import com.example.demo.model.State;
import com.example.demo.repository.MunicipalityRepository;
import com.example.demo.repository.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MunicipalityServiceImpl implements MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final StateRepository stateRepository;

    /*
     * @Override
     * public List<MunicipalityResponse> findAll() {
     * return municipalityRepository.findAll().stream()
     * .map(MunicipalityMapper::toResponse)
     * .toList();
     * }
     */

    @Override
    public List<MunicipalityResponse> findAllWithPagination(int page, int pageSize) {
        return municipalityRepository.findAllWithPagination(page, pageSize).stream()
                .map(MunicipalityMapper::toResponse)
                .toList();
    }

    @Override
    public MunicipalityResponse findById(Integer id) {
        Municipality municipality = municipalityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Municipality not found: " + id));
        return MunicipalityMapper.toResponse(municipality);
    }

    @Override
    public MunicipalityResponse create(Integer stateId, MunicipalityRequest request) {
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new EntityNotFoundException("State not found: " + stateId));
        Municipality entity = MunicipalityMapper.toEntity(request);
        entity.setState(state);
        Municipality saved = municipalityRepository.save(entity);
        return MunicipalityMapper.toResponse(saved);
    }

    @Override
    public MunicipalityResponse update(Integer id, Integer stateId, MunicipalityRequest request) {
        Municipality existing = municipalityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Municipality not found: " + id));
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new EntityNotFoundException("State not found: " + stateId));
        MunicipalityMapper.copyToEntity(request, existing);
        existing.setState(state);
        Municipality updated = municipalityRepository.save(existing);
        return MunicipalityMapper.toResponse(updated);
    }

    /*
     * @Override
     * public void delete(Integer id) {
     * if (!municipalityRepository.existsById(id)) {
     * throw new EntityNotFoundException("Municipality not found: " + id);
     * }
     * municipalityRepository.deleteById(id);
     * }
     */

    @Override
    public List<MunicipalityResponse> findByStateId(Integer stateId) {
        List<Municipality> results = municipalityRepository.findByStateId(stateId);

        if (results.isEmpty()) {
            throw new EntityNotFoundException("No municipalities found for stateId " + stateId);
        }

        return results.stream()
                .map(MunicipalityMapper::toResponse)
                .toList();
    }

    @Override
    public List<MunicipalityResponse> findByNameLike(String name) {
        return municipalityRepository.findByNameLike(name).stream()
                .map(MunicipalityMapper::toResponse)
                .toList();
    }
}
