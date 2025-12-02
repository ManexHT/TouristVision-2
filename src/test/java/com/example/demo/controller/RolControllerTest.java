package com.example.demo.controller;

import com.example.demo.dto.RolRequest;
import com.example.demo.dto.RolResponse;
import com.example.demo.service.RolService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RolController.class)
class RolControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RolService service;

    private static final String BASE = "/api/roles";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        RolService rolService() {
            return mock(RolService.class);
        }
    }

    // Helpers
    private RolResponse resp(int id, String name) {
        return RolResponse.builder()
                .id_rol(id)
                .name(name)
                .build();
    }

    private RolRequest req(String name) {
        RolRequest r = new RolRequest();
        r.setName(name);
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, "Admin")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Admin"));
    }

    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, "User"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // ===========================================
    // GET /search/name/{name}
    // ===========================================
    @Test
    void findByNameLike_ok() throws Exception {
        List<RolResponse> mockList = List.of(resp(1, "Admin"));
        when(service.findByNameLike("Admin")).thenReturn(mockList);

        mvc.perform(get(BASE + "/search/name/{name}", "Admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Admin"));
    }

@Test
void findByNameLike_notFound() throws Exception {
    when(service.findByNameLike("Unknown")).thenThrow(new EntityNotFoundException("Not found"));

    mvc.perform(get(BASE + "/search/name/{name}", "Unknown"))
            .andExpect(status().isNotFound());
}


    // ===========================================
    // POST /
    // ===========================================
    @Test
    void create_ok() throws Exception {
        RolRequest rq = req("Editor");
        RolResponse created = resp(10, "Editor");
        when(service.create(Mockito.any(RolRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/roles/10"))
                .andExpect(jsonPath("$.id_rol").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        RolRequest rq = req("Supervisor");
        RolResponse updated = resp(20, "Supervisor");
        when(service.update(eq(20), Mockito.any(RolRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Supervisor"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(RolRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X"))))
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
        when(service.findAll()).thenReturn(List.of(resp(1, "Admin")));

        mvc.perform(get(BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
