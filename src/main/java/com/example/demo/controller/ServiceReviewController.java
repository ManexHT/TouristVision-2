package com.example.demo.controller;

import com.example.demo.dto.ServiceReviewRequest;
import com.example.demo.dto.ServiceReviewResponse;
import com.example.demo.service.ServiceReviewService;

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

@Tag(name = "Service Review", description ="List of Service Reviews")
@RestController
@RequestMapping("/api/serviceReviews")
@RequiredArgsConstructor
public class ServiceReviewController {

    private final ServiceReviewService service;

    /*@Operation(summary = "Get all service reviews")
    @ApiResponse(responseCode = "200", description = "List of all service reviews", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceReviewResponse.class))) })
    @GetMapping
    public List<ServiceReviewResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get all service reviews with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of service reviews")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<ServiceReviewResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Get a service review by ID")
    @ApiResponse(responseCode = "200", description = "Review found")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @GetMapping("/{id}")
    public ServiceReviewResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new service review")
    @ApiResponse(responseCode = "201", description = "Review created")
    @PostMapping("/service/{serviceId}/user/{userId}")
    public ResponseEntity<ServiceReviewResponse> create(
            @PathVariable Integer serviceId,
            @PathVariable Integer userId,
            @Valid @RequestBody ServiceReviewRequest request) {
        ServiceReviewResponse created = service.create(serviceId, userId, request);
        return ResponseEntity
                .created(URI.create("/api/serviceReviews/" + created.getId_serviceReview()))
                .body(created);
    }

    @Operation(summary = "Update an existing service review")
    @ApiResponse(responseCode = "200", description = "Review updated")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @PutMapping("/{id}")
    public ServiceReviewResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody ServiceReviewRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a service review")
    @ApiResponse(responseCode = "204", description = "Review deleted")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @Operation(summary = "Find reviews by rating")
    @ApiResponse(responseCode = "200", description = "List of reviews with given rating")
    @GetMapping("/rating/{rating}")
    public List<ServiceReviewResponse> findByRating(@PathVariable Integer rating) {
        return service.findByRating(rating);
    }

    @Operation(summary = "Search reviews by comment keyword")
    @ApiResponse(responseCode = "200", description = "List of reviews matching keyword")
    @GetMapping("/search/{keyword}")
    public List<ServiceReviewResponse> searchByComment(@PathVariable String keyword) {
        return service.searchByComment(keyword);
    }
}
