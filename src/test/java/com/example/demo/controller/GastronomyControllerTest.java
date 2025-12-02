package com.example.demo.controller;

import com.example.demo.dto.GastronomyRequest;
import com.example.demo.dto.GastronomyResponse;
import com.example.demo.service.GastronomyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GastronomyController.class)
class GastronomyControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    GastronomyService service;

    private static final String BASE = "/api/gastronomies";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        GastronomyService gastronomyService() {
            return mock(GastronomyService.class);
        }
    }

    // Helpers
    private GastronomyResponse resp(int id, int placeId, String name, String desc) {
        return GastronomyResponse.builder()
                .id_dish(id)
                .id_place(placeId)
                .name(name)
                .description(desc)
                .build();
    }

    private GastronomyRequest req(String name, String desc) {
        GastronomyRequest r = new GastronomyRequest();
        r.setName(name);
        r.setDescription(desc);
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    /* 
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, 10, "Mole", "Traditional dish")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mole"));
    }
*/
    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, 10, "Tacos", "Street food"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tacos"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // ===========================================
    // GET /pagination
    // ===========================================
    @ParameterizedTest
    @CsvSource({
            "0,10",
            "1,1",
            "2,50",
            "5,5"
    })
    void pagination_ok(int page, int size) throws Exception {
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(1, 10, "Mole", "Traditional dish")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Mole"));
    }

    @ParameterizedTest(name = "GET /pagination?page={0}&pageSize={1} inválidos → 400")
    @CsvSource({
            "-1,10",
            "0,0",
            "0,-5",
            "-3,-3"
    })
    @DisplayName("GET paginado: parámetros inválidos → 400")
    void pagination_badRequest(int page, int size) throws Exception {
        when(service.findAllWithPagination(page, size))
                .thenThrow(new IllegalArgumentException("Invalid paging params"));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("")));
    }

    // ===========================================
    // POST /place/{placeId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        GastronomyRequest rq = req("Pozole", "Soup with hominy");
        GastronomyResponse created = resp(100, 10, rq.getName(), rq.getDescription());
        when(service.create(eq(10), Mockito.any(GastronomyRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/place/{placeId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/gastronomies/100"))
                .andExpect(jsonPath("$.id_dish").value(100));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/place/{placeId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        GastronomyRequest rq = req("Updated Dish", "New description");
        GastronomyResponse updated = resp(200, 10, rq.getName(), rq.getDescription());
        when(service.update(eq(200), Mockito.any(GastronomyRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 200)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Dish"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(GastronomyRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X", "Y"))))
                .andExpect(status().isNotFound());
    }

    // ===========================================
    // DELETE /{id}
    // ===========================================
    /* 
    @Test
    void delete_ok() throws Exception {
        doNothing().when(service).delete(33);

        mvc.perform(delete(BASE + "/{id}", 33))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(4040);

        mvc.perform(delete(BASE + "/{id}", 4040))
                .andExpect(status().isNotFound());
    }
*/
    // ===========================================
    // Headers CORS y negociación de contenido
    // ===========================================

    /* 
    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin presente")
    void cors_header_present() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE)
                .header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    @DisplayName("Content negotiation: JSON → application/json")
    void contentNegotiation_json() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, 10, "Mole", "Traditional dish")));

        mvc.perform(get(BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }*/
}
