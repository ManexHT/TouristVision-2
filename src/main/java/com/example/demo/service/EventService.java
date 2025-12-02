package com.example.demo.service;

import com.example.demo.dto.EventRequest;
import com.example.demo.dto.EventResponse;

import java.util.List;

public interface EventService {

    EventResponse findById(Integer id);

    EventResponse create(Integer placeId, EventRequest request);

    EventResponse update(Integer id, EventRequest request);

    void delete(Integer id);

    List<EventResponse> findAllWithPagination(int page, int pageSize);

}
