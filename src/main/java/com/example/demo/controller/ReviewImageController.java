package com.example.demo.controller;

import com.example.demo.dto.ReviewImageRequest;
import com.example.demo.dto.ReviewImageResponse;
import com.example.demo.service.ReviewImageService;

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

@Tag(name = "Review Images", description ="List of Review Images")
@RestController
@RequestMapping("/api/reviewImages")
@RequiredArgsConstructor
public class ReviewImageController {

    private final ReviewImageService service;


    @Operation(summary = "Get paginated review images")
    @ApiResponse(responseCode = "200", description = "Paginated list of review images")
    @GetMapping("/page")
    public List<ReviewImageResponse> findAllWithPagination(
            @RequestParam int page,
            @RequestParam int size) {
        return service.findAllWithPagination(page, size);
    }

    @Operation(summary = "Get a review image by ID")
    @ApiResponse(responseCode = "200", description = "Review image found")
    @ApiResponse(responseCode = "404", description = "Review image not found")
    @GetMapping("/{id}")
    public ReviewImageResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get all images for a specific review")
    @ApiResponse(responseCode = "200", description = "List of images for the review")
    @GetMapping("/review/{reviewId}")
    public List<ReviewImageResponse> findByReviewId(@PathVariable Integer reviewId) {
        return service.findByReviewId(reviewId);
    }

    @Operation(summary = "Create a new review image")
    @ApiResponse(responseCode = "201", description = "Review image created")
    @PostMapping("/review/{reviewId}")
    public ResponseEntity<ReviewImageResponse> create(
            @PathVariable Integer reviewId,
            @Valid @RequestBody ReviewImageRequest request) {
        ReviewImageResponse created = service.create(reviewId, request);
    return ResponseEntity
        .created(URI.create("/api/reviewImages/" + created.getId_image()))
        .body(created);
    }

    @Operation(summary = "Update an existing review image")
    @ApiResponse(responseCode = "200", description = "Review image updated")
    @ApiResponse(responseCode = "404", description = "Review image not found")
    @PutMapping("/{id}")
    public ReviewImageResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewImageRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a review image")
    @ApiResponse(responseCode = "204", description = "Review image deleted")
    @ApiResponse(responseCode = "404", description = "Review image not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
