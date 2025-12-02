package com.example.demo.controller;

import com.example.demo.dto.LegendRequest;
import com.example.demo.dto.LegendResponse;
import com.example.demo.service.LegendService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Legend", description ="List of Legends")
@RestController
@RequestMapping("/api/legends")
@RequiredArgsConstructor
public class LegendController {

    private final LegendService service;

    /*@Operation(summary = "Get all legends")
    @ApiResponse(responseCode = "200", description = "List of all legends", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LegendResponse.class))) })
    @GetMapping
    public List<LegendResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get a legend by ID")
    @ApiResponse(responseCode = "200", description = "Legend found")
    @ApiResponse(responseCode = "404", description = "Legend not found")
    @GetMapping("/{id}")
    public LegendResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get legends with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of legends")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<LegendResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Create a new legend for a place")
    @ApiResponse(responseCode = "201", description = "Legend created")
    @PostMapping("/place/{placeId}")
    public ResponseEntity<LegendResponse> create(
            @PathVariable Integer placeId,
            @Valid @RequestBody LegendRequest request) {
        LegendResponse created = service.create(placeId, request);
        return ResponseEntity
                .created(URI.create("/api/legends/" + created.getId_legend()))
                .body(created);
    }

    @Operation(summary = "Update an existing legend")
    @ApiResponse(responseCode = "200", description = "Legend updated")
    @ApiResponse(responseCode = "404", description = "Legend not found")
    @PutMapping("/{id}")
    public LegendResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody LegendRequest request) {
        return service.update(id, request);
    }

    /*@Operation(summary = "Delete a legend")
    @ApiResponse(responseCode = "204", description = "Legend deleted")
    @ApiResponse(responseCode = "404", description = "Legend not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/
}
