/*package com.example.demo.controller;

import com.example.demo.dto.ImageAnalysisRequest;
import com.example.demo.dto.ImageAnalysisResponse;
import com.example.demo.service.ImageAnalysisService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.URI;
import java.util.List;

@Tag(name = "Image Analysis")
@RestController
@RequestMapping("/api/imageAnalysis")
@RequiredArgsConstructor
public class ImageAnalysisController {

    private final ImageAnalysisService service;

    @Operation(summary = "Get all image analyses")
    @ApiResponse(responseCode = "200", description = "List of all image analyses", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ImageAnalysisResponse.class))) })
    @GetMapping
    public List<ImageAnalysisResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Get an image analysis by ID")
    @ApiResponse(responseCode = "200", description = "Image analysis found")
    @ApiResponse(responseCode = "404", description = "Image analysis not found")
    @GetMapping("/{id}")
    public ImageAnalysisResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get all analyses for a specific image")
    @ApiResponse(responseCode = "200", description = "List of analyses for the image")
    @GetMapping("/image/{imageId}")
    public List<ImageAnalysisResponse> findByImageId(@PathVariable Integer imageId) {
        return service.findByImageId(imageId);
    }

    @Operation(summary = "Create a new image analysis")
    @ApiResponse(responseCode = "201", description = "Image analysis created")
    @PostMapping("/image/{imageId}")
    public ResponseEntity<ImageAnalysisResponse> create(
            @PathVariable Integer imageId,
            @Valid @RequestBody ImageAnalysisRequest request) {
        ImageAnalysisResponse created = service.create(imageId, request);
        return ResponseEntity
                .created(URI.create("/api/imageAnalysis/" + created.getId_imageAnalysis()))
                .body(created);
    }

    @Operation(summary = "Update an existing image analysis")
    @ApiResponse(responseCode = "200", description = "Image analysis updated")
    @ApiResponse(responseCode = "404", description = "Image analysis not found")
    @PutMapping("/{id}")
    public ImageAnalysisResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody ImageAnalysisRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete an image analysis")
    @ApiResponse(responseCode = "204", description = "Image analysis deleted")
    @ApiResponse(responseCode = "404", description = "Image analysis not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
*/