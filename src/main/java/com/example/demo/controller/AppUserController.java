package com.example.demo.controller;

import com.example.demo.dto.AppUserRequest;
import com.example.demo.dto.AppUserResponse;
import com.example.demo.service.AppUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "AppUser", description ="List of Users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService service;
    private final PasswordEncoder passwordEncoder;

    /*@Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AppUserResponse.class))) })
    @GetMapping
    public List<AppUserResponse> findAll() {
        return service.findAll();
    }
    */

@Operation(summary = "Get paginated users (Admin only)")
@ApiResponse(responseCode = "200", description = "Users found")
@ApiResponse(responseCode = "404", description = "No users found or access denied")
@GetMapping("/users/pagination")
public List<AppUserResponse> getUsersWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize,
        @RequestParam int rolId) {
    return service.findAllWithPagination(page, pageSize, rolId);
}


    @Operation(summary = "Get a user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public AppUserResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @PostMapping("/rol/{rolId}")
    public ResponseEntity<AppUserResponse> create(
            @PathVariable Integer rolId,
            @Valid @RequestBody AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword())); //CIFRAR CONTRASEÑA
        AppUserResponse created = service.create(rolId, request);
        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId_user()))
                .body(created);
    }

    @Operation(summary = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "User updated")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/rol/{rolId}")
    public AppUserResponse update(
            @PathVariable Integer id,
            @PathVariable Integer rolId,
            @Valid @RequestBody AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return service.update(id, rolId, request);
    }

    /*@Operation(summary = "Delete a user")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
    */

    @Operation(summary = "Search users by username")
    @ApiResponse(responseCode = "200", description = "List of users matching username")
    @GetMapping("/search/username/{username}")
    public List<AppUserResponse> findByUsernameLike(@PathVariable String username) {
        return service.findByUsernameLike(username);
    }

}



