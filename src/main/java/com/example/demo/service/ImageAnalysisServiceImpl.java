/*package com.example.demo.service;

import com.example.demo.dto.ImageAnalysisRequest;
import com.example.demo.dto.ImageAnalysisResponse;
import com.example.demo.mapper.ImageAnalysisMapper;
import com.example.demo.model.ImageAnalysis;
import com.example.demo.model.ReviewImage;
import com.example.demo.repository.ImageAnalysisRepository;
import com.example.demo.repository.ReviewImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageAnalysisServiceImpl implements ImageAnalysisService {

    private final ImageAnalysisRepository repository;
    private final ReviewImageRepository imageRepo;

    @Override
    public ImageAnalysisResponse findById(Integer id) {
        ImageAnalysis entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analysis not found: " + id));
        return ImageAnalysisMapper.toResponse(entity);
    }

    @Override
    public ImageAnalysisResponse create(Integer imageId, ImageAnalysisRequest request) {
        ReviewImage image = imageRepo.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + imageId));

        ImageAnalysis entity = ImageAnalysisMapper.toEntity(request);
        entity.setImage(image);

        ImageAnalysis saved = repository.save(entity);
        return ImageAnalysisMapper.toResponse(saved);
    }

    @Override
    public ImageAnalysisResponse update(Integer id, ImageAnalysisRequest request) {
        ImageAnalysis existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analysis not found: " + id));
        ImageAnalysisMapper.copyToEntity(request, existing);
        ImageAnalysis updated = repository.save(existing);
        return ImageAnalysisMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Analysis not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<ImageAnalysisResponse> findByImageId(Integer imageId) {
        return repository.findByImageId(imageId).stream()
                .map(ImageAnalysisMapper::toResponse)
                .toList();
    }

    @Override
    public List<ImageAnalysisResponse> findAll() {
        return repository.findAll().stream()
                .map(ImageAnalysisMapper::toResponse)
                .toList();
    }
}
*/