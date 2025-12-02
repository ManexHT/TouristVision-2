package com.example.demo.controller;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Address", description ="List of Addresses")
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @Operation(summary = "Get addresses with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of addresses")
    @GetMapping(value = "/pagination", params = { "page", "pageSize" })
    public List<AddressResponse> findAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: page must be >= 0 and pageSize > 0");
        }
        return service.findAllWithPagination(page, pageSize);
    }

    @Operation(summary = "Get an address by ID")
    @ApiResponse(responseCode = "200", description = "Address found")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @GetMapping("/{id}")
    public AddressResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new address")
    @ApiResponse(responseCode = "201", description = "Address created")
    @PostMapping
    public ResponseEntity<AddressResponse> create(@Valid @RequestBody AddressRequest request) {
        AddressResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/addresses/" + created.getId_address()))
                .body(created);
    }

    @Operation(summary = "Update an existing address")
    @ApiResponse(responseCode = "200", description = "Address updated")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @PutMapping("/{id}")
    public AddressResponse update(@PathVariable Integer id, @Valid @RequestBody AddressRequest request) {
        return service.update(id, request);
    }

    /*@Operation(summary = "Delete an address")
    @ApiResponse(responseCode = "204", description = "Address deleted")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }*/

    @Operation(summary = "Search addresses by postal code")
    @ApiResponse(responseCode = "200", description = "List of addresses matching postal code")
    @GetMapping("/search/postal/{postalCode}")
    public List<AddressResponse> findByPostalCode(@PathVariable String postalCode) {
        return service.findByPostalCode(postalCode);
    }

    @Operation(summary = "Search addresses by neighborhood")
    @ApiResponse(responseCode = "200", description = "List of addresses matching neighborhood")
    @GetMapping("/search/neighborhood/{neighborhood}")
    public List<AddressResponse> findByNeighborhoodLike(@PathVariable String neighborhood) {
        return service.findByNeighborhoodLike(neighborhood);
    }

    @Operation(summary = "Search addresses by street name")
    @ApiResponse(responseCode = "200", description = "List of addresses matching street name")
    @GetMapping("/search/street/{street}")
    public List<AddressResponse> findByStreetLike(@PathVariable String street) {
        return service.findByStreetLike(street);
    }
}
