package com.example.demo.controller;

import com.example.demo.dto.GastronomyRequest;
import com.example.demo.dto.GastronomyResponse;
import com.example.demo.service.GastronomyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Gastronomy", description ="List of Gastronomies")
@RestController
@RequestMapping("/api/gastronomies")
@RequiredArgsConstructor
public class GastronomyController {

    private final GastronomyService service;

    /*@Operation(summary = "Get all dishes")
    @ApiResponse(responseCode = "200", description = "List of all dishes", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GastronomyResponse.class))) })
    @GetMapping
    public List<GastronomyResponse> findAll() {
        return service.findAll();
    }
    */

    @Operation(summary = "Get a dish by ID")
    @ApiResponse(responseCode = "200", description = "Dish found")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @GetMapping("/{id}")
    public GastronomyResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get dishes with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of dishes")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<GastronomyResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Create a new dish for a place")
    @ApiResponse(responseCode = "201", description = "Dish created")
    @PostMapping("/place/{placeId}")
    public ResponseEntity<GastronomyResponse> create(
            @PathVariable Integer placeId,
            @Valid @RequestBody GastronomyRequest request) {
        GastronomyResponse created = service.create(placeId, request);
        return ResponseEntity
                .created(URI.create("/api/gastronomies/" + created.getId_dish()))
                .body(created);
    }

    @Operation(summary = "Update an existing dish")
    @ApiResponse(responseCode = "200", description = "Dish updated")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @PutMapping("/{id}")
    public GastronomyResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody GastronomyRequest request) {
        return service.update(id, request);
    }

    /*@Operation(summary = "Delete a dish")
    @ApiResponse(responseCode = "204", description = "Dish deleted")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
    */
}
