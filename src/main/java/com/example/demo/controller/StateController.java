package com.example.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.dto.StateRequest;
import com.example.demo.dto.StateResponse;
import com.example.demo.service.StateService;

import java.net.URI;
import java.util.List;

@Tag(name = "State", description ="List of States")
@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService service;

    /*@Operation(summary = "Get all states")
    @ApiResponse(responseCode = "200", description = "List of registered states.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StateResponse.class))) })
    @GetMapping
    public List<StateResponse> findAll() {
        return service.findAll();
    }*/

    @Operation(summary = "Get all states with pagination")
    @GetMapping(value = "pagination", params = { "page", "pageSize" })
    public List<StateResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Get a state by ID")
    @GetMapping("/{id}")
    public StateResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new state")
    @PostMapping
    public ResponseEntity<StateResponse> create(@Valid @RequestBody StateRequest request) {
        StateResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/states/" + created.getId_state()))
                .body(created);
    }

    @Operation(summary = "Update an existing state by ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StateResponse update(@PathVariable Integer id, @Valid @RequestBody StateRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Search states by name")
    @GetMapping("/search/{name}")
    public List<StateResponse> findByNameLike(@PathVariable String name) {
        return service.findByNameLike(name);
    }

        /*@Operation(summary = "Delete a state by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/
}
