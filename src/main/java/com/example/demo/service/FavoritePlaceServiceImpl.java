package com.example.demo.service;

import com.example.demo.dto.FavoritePlaceRequest;
import com.example.demo.dto.FavoritePlaceResponse;
import com.example.demo.mapper.FavoritePlaceMapper;
import com.example.demo.model.AppUser;
import com.example.demo.model.FavoritePlace;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.FavoritePlaceRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritePlaceServiceImpl implements FavoritePlaceService {

    private final FavoritePlaceRepository repository;
    private final AppUserRepository userRepo;
    private final TouristPlaceRepository placeRepo;

    @Override
    public FavoritePlaceResponse findById(Integer id) {
        FavoritePlace entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found: " + id));
        return FavoritePlaceMapper.toResponse(entity);
    }

    @Override
    public FavoritePlaceResponse create(FavoritePlaceRequest request) {
        AppUser user = userRepo.findById(request.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getIdUser()));
        TouristPlace place = placeRepo.findById(request.getIdPlace())
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + request.getIdPlace()));

        FavoritePlace existing = repository.findByUserIdAndPlaceId(user.getId(), place.getId());
        if (existing != null) {
            throw new IllegalArgumentException("Place already marked as favorite");
        }

        FavoritePlace entity = FavoritePlaceMapper.toEntity(request);
        entity.setUser(user);
        entity.setPlace(place);

        FavoritePlace saved = repository.save(entity);
        return FavoritePlaceMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Favorite not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<FavoritePlaceResponse> findByUserId(Integer userId) {
        return repository.findByUserId(userId).stream()
                .map(FavoritePlaceMapper::toResponse)
                .toList();
    }

    @Override
    public boolean isFavorite(Integer userId, Integer placeId) {
        return repository.findByUserIdAndPlaceId(userId, placeId) != null;
    }
}
