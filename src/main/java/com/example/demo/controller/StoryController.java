package com.example.demo.controller;

import com.example.demo.dto.StoryRequest;
import com.example.demo.dto.StoryResponse;
import com.example.demo.service.StoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Story", description ="List of Stories")
@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService service;

    /*@Operation(summary = "Get all stories")
    @ApiResponse(responseCode = "200", description = "List of all stories", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StoryResponse.class))) })
    @GetMapping
    public List<StoryResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get a story by ID")
    @ApiResponse(responseCode = "200", description = "Story found")
    @ApiResponse(responseCode = "404", description = "Story not found")
    @GetMapping("/{id}")
    public StoryResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get stories with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of stories")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<StoryResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Create a new story for a place")
    @ApiResponse(responseCode = "201", description = "Story created")
    @PostMapping("/place/{placeId}")
    public ResponseEntity<StoryResponse> create(
            @PathVariable Integer placeId,
            @Valid @RequestBody StoryRequest request) {
        StoryResponse created = service.create(placeId, request);
        return ResponseEntity
                .created(URI.create("/api/stories/" + created.getId_story()))
                .body(created);
    }

    /*@Operation(summary = "Update an existing story")
    @ApiResponse(responseCode = "200", description = "Story updated")
    @ApiResponse(responseCode = "404", description = "Story not found")
    @PutMapping("/{id}")
    public StoryResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody StoryRequest request) {
        return service.update(id, request);
    }*/

    /*@Operation(summary = "Delete a story")
    @ApiResponse(responseCode = "204", description = "Story deleted")
    @ApiResponse(responseCode = "404", description = "Story not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/
}
