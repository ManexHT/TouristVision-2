/*package com.example.demo.controller;

import com.example.demo.dto.TextAnalysisRequest;
import com.example.demo.dto.TextAnalysisResponse;
import com.example.demo.service.TextAnalysisService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Text Analysis")
@RestController
@RequestMapping("/api/textAnalysis")
@RequiredArgsConstructor
public class TextAnalysisController {

    private final TextAnalysisService service;

    @Operation(summary = "Get all text analysis")
    @ApiResponse(responseCode = "200", description = "List of all text analyses")
    @GetMapping
    public List<TextAnalysisResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Get a text analysis by ID")
    @ApiResponse(responseCode = "200", description = "Text analysis found")
    @ApiResponse(responseCode = "404", description = "Text analysis not found")
    @GetMapping("/{id}")
    public TextAnalysisResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get text analysis by review ID")
    @ApiResponse(responseCode = "200", description = "Text analysis for review found")
    @ApiResponse(responseCode = "404", description = "Text analysis not found")
    @GetMapping("/review/{reviewId}")
    public TextAnalysisResponse findByReviewId(@PathVariable Integer reviewId) {
        return service.findByReviewId(reviewId);
    }

    @Operation(summary = "Create a text analysis for a review")
    @ApiResponse(responseCode = "201", description = "Text analysis created")
    @PostMapping("/review/{reviewId}")
    public ResponseEntity<TextAnalysisResponse> create(
            @PathVariable Integer reviewId,
            @Valid @RequestBody TextAnalysisRequest request) {
        TextAnalysisResponse created = service.create(reviewId, request);
    return ResponseEntity
        .created(URI.create("/api/textAnalysis/" + created.getId_textAnalysis()))
        .body(created);
    }

    @Operation(summary = "Update an existing text analysis")
    @ApiResponse(responseCode = "200", description = "Text analysis updated")
    @ApiResponse(responseCode = "404", description = "Text analysis not found")
    @PutMapping("/{id}")
    public TextAnalysisResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody TextAnalysisRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a text analysis")
    @ApiResponse(responseCode = "204", description = "Text analysis deleted")
    @ApiResponse(responseCode = "404", description = "Text analysis not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}*/
