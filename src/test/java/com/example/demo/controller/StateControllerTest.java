package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.StateRequest;
import com.example.demo.dto.StateResponse;

import com.example.demo.service.StateService;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ==========================
 * TEST: StateController
 * ==========================
 */

@WebMvcTest(controllers = StateController.class)
class StateControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    StateService service;

    private static final String BASE = "/api/states";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    // ===========================================
    // Configuración de beans mockeados
    // ===========================================
    @TestConfiguration
    static class TestConfig {
        @Bean
        StateService stateService() {
            return mock(StateService.class);
        }
    }

    // ===========================================
    // Helpers para DTOs
    // ===========================================
    private StateResponse resp(int id, String name) {
        return StateResponse.builder().id_state(id).name(name).build();
    }

    private StateRequest req(String name) {
        StateRequest r = new StateRequest();
        r.setName(name);
        return r;
    }

    // ===========================================
    // GET /api/states
    // ===========================================
    /* 
    @Test
    @DisplayName("GET /api/states → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, "Puebla"), resp(2, "Veracruz")));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id_state").value(1))
                .andExpect(jsonPath("$[0].name").value("Puebla"))
                .andExpect(jsonPath("$[1].id_state").value(2));
    }

    @Test
    @DisplayName("GET /api/states → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
*/
    // ===========================================
    // GET /api/states/pagination?page=&pageSize=
    // ===========================================
    @ParameterizedTest(name = "GET /pagination?page={0}&pageSize={1} → 200")
    @CsvSource({
            "0,10",
            "1,1",
            "2,50",
            "5,5"
    })
    @DisplayName("GET paginado: parámetros válidos")
    void pagination_ok(int page, int size) throws Exception {
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(100, "Yucatán")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id_state").value(100))
                .andExpect(jsonPath("$[0].name").value("Yucatán"));
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
        when(service.findAllWithPagination(page, size)).thenThrow(new IllegalArgumentException("Invalid paging params"));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("Invalid Request Parameters")));
    }

    // ===========================================
    // GET /api/states/{id}
    // ===========================================
    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(7)).thenReturn(resp(7, "Chiapas"));

        mvc.perform(get(BASE + "/{id}", 7))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_state").value(7))
                .andExpect(jsonPath("$.name").value("Chiapas"));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("State not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findById_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // POST /api/states
    // ===========================================
    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        StateRequest rq = req("Nuevo León");
        StateResponse created = resp(1234, "Nuevo León");
        when(service.create(any(StateRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/states/1234"))
                .andExpect(jsonPath("$.id_state").value(1234))
                .andExpect(jsonPath("$.name").value("Nuevo León"));
    }

    @Test
    @DisplayName("POST create inválido → 400 por @Valid")
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ===========================================
    // PUT /api/states/{id}
    // ===========================================
    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        StateRequest rq = req("Estado Editado");
        StateResponse updated = resp(55, "Estado Editado");
        when(service.update(eq(55), any(StateRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 55)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_state").value(55))
                .andExpect(jsonPath("$.name").value("Estado Editado"));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999), any(StateRequest.class)))
                .thenThrow(new EntityNotFoundException("State not found"));

        mvc.perform(put(BASE + "/{id}", 9999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT update con body inválido → 400 por @Valid")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ===========================================
    // DELETE /api/states/{id}
    // ===========================================
    /*@Test
    @DisplayName("DELETE existente → 204 sin body")
    void delete_ok() throws Exception {
        doNothing().when(service).delete(33);

        mvc.perform(delete(BASE + "/{id}", 33))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("DELETE no existente → 404")
    void delete_notFound() throws Exception {
        doThrow(new EntityNotFoundException("State not found")).when(service).delete(4040);

        mvc.perform(delete(BASE + "/{id}", 4040))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }*/

    // ===========================================
    // GET /api/states/search/{name}
    // ===========================================
    @Test
    @DisplayName("GET search con resultados → 200 y lista")
    void search_ok() throws Exception {
        when(service.findByNameLike("chi"))
                .thenReturn(List.of(resp(1, "Chiapas"), resp(2, "Chihuahua")));

        mvc.perform(get(BASE + "/search/{name}", "chi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("chi")));
    }

    @Test
    @DisplayName("GET search sin resultados → 200 y []")
    void search_empty() throws Exception {
        when(service.findByNameLike("zzz")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/search/{name}", "zzz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===========================================
    // Headers CORS y negociación de contenido
    // ===========================================
    /* 
    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin presente")
    void cors_header_present() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE).header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    @DisplayName("Content negotiation: JSON → application/json")
    void contentNegotiation_json() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, "Aguascalientes")));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }*/
}
