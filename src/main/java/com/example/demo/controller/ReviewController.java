package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.service.ReviewService;

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

@Tag(name = "Review", description ="List of Reviews")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    /*@Operation(summary = "Get all reviews")
    @ApiResponse(responseCode = "200", description = "List of all reviews", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReviewResponse.class))) })
    @GetMapping
    public List<ReviewResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get a review by ID")
    @ApiResponse(responseCode = "200", description = "Review found")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @GetMapping("/{id}")
    public ReviewResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }


    @Operation(summary = "Get all paginated reviews")
    @ApiResponse(responseCode = "200", description = "Paginated list of reviews")
    @GetMapping(value = "pagination", params = { "page", "pageSize" })
    public List<ReviewResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    /*@Operation(summary = "Get paginated reviews for a place")
    @ApiResponse(responseCode = "200", description = "Paginated list of reviews for the place")
    @GetMapping("/place/{placeId}")
    public List<ReviewResponse> findByPlaceId(
            @PathVariable Integer placeId,
            @RequestParam int page,
            @RequestParam int size) {
        return service.findByPlaceId(placeId, page, size);
    }*/

    @Operation(summary = "Get paginated reviews by a user")
    @ApiResponse(responseCode = "200", description = "Paginated list of reviews by the user")
    @GetMapping("/user/{userId}")
    public List<ReviewResponse> findByUserId(
            @PathVariable Integer userId,
            @RequestParam int page,
            @RequestParam int size) {
        return service.findByUserId(userId, page, size);
    }

    @Operation(summary = "Create a new review")
    @ApiResponse(responseCode = "201", description = "Review created")
    @PostMapping("/user/{userId}/place/{placeId}")
    public ResponseEntity<ReviewResponse> create(
            @PathVariable Integer userId,
            @PathVariable Integer placeId,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse created = service.create(userId, placeId, request);
        return ResponseEntity
                .created(URI.create("/api/reviews/" + created.getId_review()))
                .body(created);
    }

    @Operation(summary = "Update an existing review")
    @ApiResponse(responseCode = "200", description = "Review updated")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @PutMapping("/{id}")
    public ReviewResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a review")
    @ApiResponse(responseCode = "204", description = "Review deleted")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
