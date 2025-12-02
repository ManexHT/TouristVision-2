package com.example.demo.controller;

import com.example.demo.dto.FavoritePlaceRequest;
import com.example.demo.dto.FavoritePlaceResponse;
import com.example.demo.service.FavoritePlaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FavoritePlaceController.class)
class FavoritePlaceControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    FavoritePlaceService service;

    private static final String BASE = "/api/favorites";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        FavoritePlaceService favoritePlaceService() {
            return mock(FavoritePlaceService.class);
        }
    }

    // Helpers
    private FavoritePlaceResponse resp(int id, int userId, int placeId) {
        return FavoritePlaceResponse.builder()
                .id_favorite(id)
                .id_user(userId)
                .id_place(placeId)
                .build();
    }

    private FavoritePlaceRequest req(int userId, int placeId) {
        FavoritePlaceRequest r = new FavoritePlaceRequest();
        r.setIdUser(userId);
        r.setIdPlace(placeId);
        return r;
    }

    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, 1, 5));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_user").value(1))
                .andExpect(jsonPath("$.id_place").value(5));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findById_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // GET /user/{userId}
    // ===========================================
    @Test
    void findByUserId_ok() throws Exception {
        when(service.findByUserId(1)).thenReturn(List.of(resp(1, 1, 10)));

        mvc.perform(get(BASE + "/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_place").value(10));
    }


    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findByUserId_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // GET /check?userId=&placeId=
    // ===========================================
    @Test
    void isFavorite_true() throws Exception {
        when(service.isFavorite(1, 10)).thenReturn(true);

        mvc.perform(get(BASE + "/check")
                .queryParam("userId", "1")
                .queryParam("placeId", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isFavorite_false() throws Exception {
        when(service.isFavorite(1, 99)).thenReturn(false);

        mvc.perform(get(BASE + "/check")
                .queryParam("userId", "1")
                .queryParam("placeId", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ===========================================
    // POST /
    // ===========================================
    @Test
    void create_ok() throws Exception {
        FavoritePlaceRequest rq = req(1, 10);
        FavoritePlaceResponse created = resp(100, 1, 10);
        when(service.create(Mockito.any(FavoritePlaceRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/favorites/100"))
                .andExpect(jsonPath("$.id_favorite").value(100));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // DELETE /{id}
    // ===========================================
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

    // ===========================================
    // Headers CORS y negociación de contenido
    // ===========================================
    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin presente")
    void cors_header_present() throws Exception {
        when(service.findByUserId(1)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/user/{userId}", 1)
                .header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    @DisplayName("Content negotiation: JSON → application/json")
    void contentNegotiation_json() throws Exception {
        when(service.findByUserId(1)).thenReturn(List.of(resp(1, 1, 10)));

        mvc.perform(get(BASE + "/user/{userId}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
