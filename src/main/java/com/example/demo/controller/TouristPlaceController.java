package com.example.demo.controller;

import com.example.demo.dto.TouristPlaceRequest;
import com.example.demo.dto.TouristPlaceResponse;
import com.example.demo.service.TouristPlaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Tourist Place", description ="List of Places")
@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class TouristPlaceController {

    private final TouristPlaceService service;

    /*@Operation(summary = "Get all tourist places")
    @ApiResponse(responseCode = "200", description = "List of all tourist places", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TouristPlaceResponse.class))) })
    @GetMapping
    public List<TouristPlaceResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get tourist places with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of tourist places")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<TouristPlaceResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Get a tourist place by ID")
    @ApiResponse(responseCode = "200", description = "Tourist place found")
    @ApiResponse(responseCode = "404", description = "Tourist place not found")
    @GetMapping("/{id}")
    public TouristPlaceResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new tourist place")
    @ApiResponse(responseCode = "201", description = "Tourist place created")
    @PostMapping("/municipality/{municipalityId}/address/{addressId}")
    public ResponseEntity<TouristPlaceResponse> create(
            @PathVariable Integer municipalityId,
            @PathVariable Integer addressId,
            @Valid @RequestBody TouristPlaceRequest request) {
        TouristPlaceResponse created = service.create(municipalityId, addressId, request);
        return ResponseEntity
                .created(URI.create("/api/places/" + created.getId_place()))
                .body(created);
    }

    @Operation(summary = "Update an existing tourist place")
    @ApiResponse(responseCode = "200", description = "Tourist place updated")
    @ApiResponse(responseCode = "404", description = "Tourist place not found")
    @PutMapping("/{id}/municipality/{municipalityId}/address/{addressId}")
    public TouristPlaceResponse update(
            @PathVariable Integer id,
            @PathVariable Integer municipalityId,
            @PathVariable Integer addressId,
            @Valid @RequestBody TouristPlaceRequest request) {
        return service.update(id, municipalityId, addressId, request);
    }

    /*@Operation(summary = "Delete a tourist place")
    @ApiResponse(responseCode = "204", description = "Tourist place deleted")
    @ApiResponse(responseCode = "404", description = "Tourist place not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/

    @Operation(summary = "Search tourist places by name")
    @ApiResponse(responseCode = "200", description = "List of tourist places matching name")
    @GetMapping("/search/name/{name}")
    public List<TouristPlaceResponse> findByNameLike(@PathVariable String name) {
        return service.findByNameLike(name);
    }

    @Operation(summary = "Search tourist places by municipality ID")
    @ApiResponse(responseCode = "200", description = "List of tourist places in the given municipality")
    @GetMapping("/search/municipality/{municipalityId}")
    public List<TouristPlaceResponse> findByMunicipalityId(@PathVariable Integer municipalityId) {
        return service.findByMunicipalityId(municipalityId);
    }
}