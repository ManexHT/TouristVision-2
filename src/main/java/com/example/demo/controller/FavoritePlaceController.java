package com.example.demo.controller;

import com.example.demo.dto.FavoritePlaceRequest;
import com.example.demo.dto.FavoritePlaceResponse;
import com.example.demo.service.FavoritePlaceService;

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

@Tag(name = "Favorite Place", description ="List of Favorites")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoritePlaceController {

    private final FavoritePlaceService service;

    @Operation(summary = "Get a favorite by ID")
    @ApiResponse(responseCode = "200", description = "Favorite found")
    @ApiResponse(responseCode = "404", description = "Favorite not found")
    @GetMapping("/{id}")
    public FavoritePlaceResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get all favorite places for a user")
    @ApiResponse(responseCode = "200", description = "List of favorite places")
    @GetMapping("/user/{userId}")
    public List<FavoritePlaceResponse> findByUserId(@PathVariable Integer userId) {
        return service.findByUserId(userId);
    }

    @Operation(summary = "Check if a place is favorited by a user")
    @ApiResponse(responseCode = "200", description = "True if place is favorited")
    @GetMapping("/check")
    public boolean isFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer placeId) {
        return service.isFavorite(userId, placeId);
    }

    @Operation(summary = "Add a place to favorites")
    @ApiResponse(responseCode = "201", description = "Favorite created")
    @PostMapping
    public ResponseEntity<FavoritePlaceResponse> create(@Valid @RequestBody FavoritePlaceRequest request) {
        FavoritePlaceResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/favorites/" + created.getId_favorite()))
                .body(created);
    }

    @Operation(summary = "Remove a favorite by ID")
    @ApiResponse(responseCode = "204", description = "Favorite deleted")
    @ApiResponse(responseCode = "404", description = "Favorite not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
