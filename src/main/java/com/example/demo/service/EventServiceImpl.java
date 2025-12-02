package com.example.demo.service;

import com.example.demo.dto.EventRequest;
import com.example.demo.dto.EventResponse;
import com.example.demo.mapper.EventMapper;
import com.example.demo.model.Event;
import com.example.demo.model.TouristPlace;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.TouristPlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final TouristPlaceRepository placeRepo;

    @Override
    public EventResponse findById(Integer id) {
        Event entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + id));
        return EventMapper.toResponse(entity);
    }

    @Override
    public EventResponse create(Integer placeId, EventRequest request) {
        TouristPlace place = placeRepo.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found: " + placeId));

        Event entity = EventMapper.toEntity(request);
        entity.setPlace(place);

        Event saved = repository.save(entity);
        return EventMapper.toResponse(saved);
    }

    @Override
    public EventResponse update(Integer id, EventRequest request) {
        Event existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + id));
        EventMapper.copyToEntity(request, existing);
        Event updated = repository.save(existing);
        return EventMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Event not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<EventResponse> findAllWithPagination(int page, int pageSize) {
        int offset = Math.max(page, 0);
        int limit = Math.max(pageSize, 1);
        return repository.findAllWithPagination(offset, limit).stream()
                .map(EventMapper::toResponse)
                .toList();
    }

}
