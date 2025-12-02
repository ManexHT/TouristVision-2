package com.example.demo.controller;

import com.example.demo.dto.AppUserRequest;
import com.example.demo.dto.AppUserResponse;
import com.example.demo.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
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
import static org.hamcrest.Matchers.containsStringIgnoringCase;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppUserController.class)
class AppUserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AppUserService service;

    private static final String BASE = "/api/users";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        AppUserService appUserService() {
            return mock(AppUserService.class);
        }
    }

    // Helpers
    private AppUserResponse resp(int id, String username) {
        return AppUserResponse.builder()
                .id_user(id)
                .username(username)
                .rolId(1)
                .build();
    }

    private AppUserRequest req(String username) {
        AppUserRequest r = new AppUserRequest();
        r.setUsername(username);
        r.setPassword("secure123");
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    /* 
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, "admin")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }
    */
    // ===========================================
    // GET /pagination
    // ===========================================
    @ParameterizedTest
    @CsvSource({
            "0,10,2",
            "1,1,2",
            "2,50,2",
            "5,5,2"
    })
    void pagination_ok_forAdmin(int page, int size, int rolId) throws Exception {
        when(service.findAllWithPagination(page, size, rolId)).thenReturn(List.of(resp(1, "admin")));

        mvc.perform(get(BASE + "/users/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .queryParam("rolId", String.valueOf(rolId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @ParameterizedTest(name = "GET /pagination?page={0}&pageSize={1} inválidos → 400")
    @CsvSource({
            "-1,10,1",
            "0,0,1",
            "0,-5,1",
            "-3,-3,1"
    })
    void pagination_badRequest(int page, int size, int rolId) throws Exception {
        when(service.findAllWithPagination(page, size, rolId))
                .thenThrow(new IllegalArgumentException("Invalid paging params"));

        mvc.perform(get(BASE + "/users/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .queryParam("rolId", String.valueOf(rolId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("")));
    }


    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, "user3"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user3"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // ===========================================
    // POST /rol/{rolId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        AppUserRequest rq = req("newuser");
        AppUserResponse created = resp(10, "newuser");
        when(service.create(eq(1), Mockito.any(AppUserRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/rol/{rolId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/10"))
                .andExpect(jsonPath("$.id_user").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/rol/{rolId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}/rol/{rolId}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        AppUserRequest rq = req("updateduser");
        AppUserResponse updated = resp(20, "updateduser");
        when(service.update(eq(20), eq(1), Mockito.any(AppUserRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}/rol/{rolId}", 20, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), eq(1), Mockito.any(AppUserRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}/rol/{rolId}", 999, 1)
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
    // GET /search/username/{username}
    // ===========================================
    @Test
    void findByUsernameLike_ok() throws Exception {
        when(service.findByUsernameLike("admin")).thenReturn(List.of(resp(1, "admin")));

        mvc.perform(get(BASE + "/search/username/{username}", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", containsStringIgnoringCase("admin")));
    }

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
        when(service.findAll()).thenReturn(List.of(resp(1, "admin")));

        mvc.perform(get(BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }*/
}
