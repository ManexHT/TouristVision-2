package com.example.demo.controller;

import com.example.demo.dto.MunicipalityRequest;
import com.example.demo.dto.MunicipalityResponse;
import com.example.demo.service.MunicipalityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;

@Tag(name = "Municipality", description ="List of Municipalities")
@RestController
@RequestMapping("/api/municipalities")
@RequiredArgsConstructor
public class MunicipalityController {

    private final MunicipalityService service;

    /*@Operation(summary = "Get all municipalities")
    @ApiResponse(responseCode = "200", description = "List of registered municipalities.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MunicipalityResponse.class))) })
    @GetMapping
    public List<MunicipalityResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get all municipalities with pagination")
    @GetMapping(value = "pagination", params = { "page", "pageSize" })
    public List<MunicipalityResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Get a municipality by ID")
    @GetMapping("/{id}")
    public MunicipalityResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new municipality")
    @PostMapping("/state/{stateId}")
    public ResponseEntity<MunicipalityResponse> create(
            @PathVariable Integer stateId,
            @Valid @RequestBody MunicipalityRequest request) {
        MunicipalityResponse created = service.create(stateId, request);
        return ResponseEntity
                .created(URI.create("/api/municipalities/" + created.getId_municipality()))
                .body(created);
    }

    @Operation(summary = "Update an existing municipality by ID")
    @PutMapping("/{id}/state/{stateId}")
    public MunicipalityResponse update(
            @PathVariable Integer id,
            @PathVariable Integer stateId,
            @Valid @RequestBody MunicipalityRequest request) {
        return service.update(id, stateId, request);
    }

    /*@Operation(summary = "Delete a municipality by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/

    @Operation(summary = "Get municipalities by state ID")
    @GetMapping("/state/{stateId}")
    public List<MunicipalityResponse> findByStateId(@PathVariable Integer stateId) {
        return service.findByStateId(stateId);
    }

    @Operation(summary = "Search municipalities by name")
    @GetMapping("/search/{name}")
    public List<MunicipalityResponse> findByNameLike(@PathVariable String name) {
        return service.findByNameLike(name);
    }
}
