package com.example.demo.controller;

import com.example.demo.dto.ServicesRequest;
import com.example.demo.dto.ServicesResponse;
import com.example.demo.service.ServicesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Service", description ="List of Services")
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final ServicesService service;


    @Operation(summary = "Get all services with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of services")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<ServicesResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Get a service by ID")
    @ApiResponse(responseCode = "200", description = "Service found")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @GetMapping("/{id}")
    public ServicesResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new service")
    @ApiResponse(responseCode = "201", description = "Service created")
    @PostMapping("/place/{placeId}/address/{addressId}")
    public ResponseEntity<ServicesResponse> create(
            @PathVariable Integer placeId,
            @PathVariable Integer addressId,
            @Valid @RequestBody ServicesRequest request) {
        ServicesResponse created = service.create(placeId, addressId, request);
        return ResponseEntity
                .created(URI.create("/api/services/" + created.getId_service()))
                .body(created);
    }

    @Operation(summary = "Update an existing service")
    @ApiResponse(responseCode = "200", description = "Service updated")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @PutMapping("/{id}/place/{placeId}/address/{addressId}")
    public ServicesResponse update(
            @PathVariable Integer id,
            @PathVariable Integer placeId,
            @PathVariable Integer addressId,
            @Valid @RequestBody ServicesRequest request) {
        return service.update(id, placeId, addressId, request);
    }

    /*@Operation(summary = "Delete a service")
    @ApiResponse(responseCode = "204", description = "Service deleted")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/

    @Operation(summary = "Search services by name")
    @ApiResponse(responseCode = "200", description = "List of services matching name")
    @GetMapping("/search/name/{name}")
    public List<ServicesResponse> findByNameLike(@PathVariable String name) {
        return service.findByNameLike(name);
    }

    @Operation(summary = "Search services by type")
    @ApiResponse(responseCode = "200", description = "List of services matching type")
    @GetMapping("/search/type/{type}")
    public List<ServicesResponse> findByType(@PathVariable String type) {
        return service.findByType(type);
    }

    @Operation(summary = "Search services by price range")
    @ApiResponse(responseCode = "200", description = "List of services matching price range")
    @GetMapping("/search/price/{range}")
    public List<ServicesResponse> findByPriceRange(@PathVariable String range) {
        return service.findByPriceRange(range);
    }

    @Operation(summary = "Search services by tourist place name")
    @ApiResponse(responseCode = "200", description = "List of services matching tourist place name")
    @GetMapping("/search/place-name/{placeName}")
    public List<ServicesResponse> findByTouristPlaceName(@PathVariable String placeName) {
        return service.findByTouristPlaceName(placeName);
    }

}
