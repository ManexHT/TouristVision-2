package com.example.demo.controller;

import com.example.demo.dto.RolRequest;
import com.example.demo.dto.RolResponse;
import com.example.demo.service.RolService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

@Tag(name = "Rol", description ="List of Roles")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService service;

    @Operation(summary = "Get all roles")
    @ApiResponse(responseCode = "200", description = "List of all roles", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RolResponse.class))) })
    @GetMapping
    public List<RolResponse> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Get a role by ID")
    @ApiResponse(responseCode = "200", description = "Role found")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @GetMapping("/{id}")
    public RolResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Get roles by partial name match")
    @ApiResponse(responseCode = "200", description = "Roles found")
    @ApiResponse(responseCode = "404", description = "No roles found")
    @GetMapping("/search/name/{name}")
    public List<RolResponse> findByNameLike(@PathVariable String name) {
        return service.findByNameLike(name);
    }

    @Operation(summary = "Create a new role")
    @ApiResponse(responseCode = "201", description = "Role created")
    @PostMapping
    public ResponseEntity<RolResponse> create(@Valid @RequestBody RolRequest request) {
        RolResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/roles/" + created.getId_rol()))
                .body(created);
    }

    @Operation(summary = "Update an existing role")
    @ApiResponse(responseCode = "200", description = "Role updated")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PutMapping("/{id}")
    public RolResponse update(@PathVariable Integer id, @Valid @RequestBody RolRequest request) {
        return service.update(id, request);
    }

    /*@Operation(summary = "Delete a role")
    @ApiResponse(responseCode = "204", description = "Role deleted")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/
}