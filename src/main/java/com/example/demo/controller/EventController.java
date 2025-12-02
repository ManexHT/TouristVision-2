package com.example.demo.controller;

import com.example.demo.dto.EventRequest;
import com.example.demo.dto.EventResponse;
import com.example.demo.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Event", description ="List of Events")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @Operation(summary = "Get a specific event by ID")
    @ApiResponse(responseCode = "200", description = "Event found")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @GetMapping("/{id}")
    public EventResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get events with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of events")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<EventResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Create a new event for a place")
    @ApiResponse(responseCode = "201", description = "Event created")
    @PostMapping("/place/{placeId}")
    public ResponseEntity<EventResponse> create(
            @PathVariable Integer placeId,
            @Valid @RequestBody EventRequest request) {
        EventResponse created = service.create(placeId, request);
        return ResponseEntity
                .created(URI.create("/api/events/" + created.getId_event()))
                .body(created);
    }

    @Operation(summary = "Update an existing event")
    @ApiResponse(responseCode = "200", description = "Event updated")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @PutMapping("/{id}")
    public EventResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody EventRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete an event")
    @ApiResponse(responseCode = "204", description = "Event deleted")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
