package com.example.demo.controller;

import com.example.demo.dto.TransportOptionRequest;
import com.example.demo.dto.TransportOptionResponse;
import com.example.demo.service.TransportOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Transport", description ="List of Transports")
@RestController
@RequestMapping("/api/transports")
@RequiredArgsConstructor
public class TransportOptionController {

    private final TransportOptionService service;

    /*@Operation(summary = "Get all transport options")
    @ApiResponse(responseCode = "200", description = "List of all transport options", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TransportOptionResponse.class))) })
    @GetMapping
    public List<TransportOptionResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get a transport option by ID")
    @ApiResponse(responseCode = "200", description = "Transport option found")
    @ApiResponse(responseCode = "404", description = "Transport option not found")
    @GetMapping("/{id}")
    public TransportOptionResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get transport options with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of transport options")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<TransportOptionResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Search transport options by type")
    @ApiResponse(responseCode = "200", description = "List of transport options matching type")
    @GetMapping("/search")
    public List<TransportOptionResponse> findByType(@RequestParam String type) {
        return service.findByType(type);
    }

    @Operation(summary = "Create a new transport option for a place")
    @ApiResponse(responseCode = "201", description = "Transport option created")
    @PostMapping("/place/{placeId}")
    public ResponseEntity<TransportOptionResponse> create(
            @PathVariable Integer placeId,
            @Valid @RequestBody TransportOptionRequest request) {
        TransportOptionResponse created = service.create(placeId, request);
        return ResponseEntity
                .created(URI.create("/api/transports/" + created.getId_transport()))
                .body(created);
    }

    @Operation(summary = "Update an existing transport option")
    @ApiResponse(responseCode = "200", description = "Transport option updated")
    @ApiResponse(responseCode = "404", description = "Transport option not found")
    @PutMapping("/{id}")
    public TransportOptionResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody TransportOptionRequest request) {
        return service.update(id, request);
    }

    /*@Operation(summary = "Delete a transport option")
    @ApiResponse(responseCode = "204", description = "Transport option deleted")
    @ApiResponse(responseCode = "404", description = "Transport option not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/
}
