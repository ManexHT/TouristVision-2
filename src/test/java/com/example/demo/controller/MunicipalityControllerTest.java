package com.example.demo.controller;

import com.example.demo.dto.MunicipalityRequest;
import com.example.demo.dto.MunicipalityResponse;
import com.example.demo.service.MunicipalityService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MunicipalityController.class)
class MunicipalityControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MunicipalityService service;

    private static final String BASE = "/api/municipalities";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        MunicipalityService municipalityService() {
            return mock(MunicipalityService.class);
        }
    }

    // Helpers
    private MunicipalityResponse resp(int id, String name, int stateId) {
        return MunicipalityResponse.builder()
                .id_municipality(id)
                .name(name)
                .stateId(stateId)
                .build();
    }

    private MunicipalityRequest req(String name) {
        MunicipalityRequest r = new MunicipalityRequest();
        r.setName(name);
        return r;
    }

    // GET /api/municipalities
    /* 
    @Test
    @DisplayName("GET /api/municipalities → 200 con lista")
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, "Teziutlán", 7)));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id_municipality").value(1))
                .andExpect(jsonPath("$[0].name").value("Teziutlán"))
                .andExpect(jsonPath("$[0].stateId").value(7));
    }

    @Test
    @DisplayName("GET /api/municipalities → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
*/
    // GET paginado
    @ParameterizedTest(name = "GET /pagination?page={0}&pageSize={1} → 200")
    @CsvSource({"0,10"
                , "1,1",
                    "2,50",
                        "5,5"})
    void pagination_ok(int page, int size) throws Exception {
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(2, "Zacatlán", 7)));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id_municipality").value(2));
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

    // GET por ID
    @Test
    void findById_ok() throws Exception {
        when(service.findById(5)).thenReturn(resp(5, "Huauchinango", 7));

        mvc.perform(get(BASE + "/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_municipality").value(5));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Municipality not found"));

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

    // POST
    @Test
    void create_ok() throws Exception {
        MunicipalityRequest rq = req("Xicotepec");
        MunicipalityResponse created = resp(10, "Xicotepec", 7);
        when(service.create(eq(7), any(MunicipalityRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/state/{stateId}", 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/municipalities/10"))
                .andExpect(jsonPath("$.id_municipality").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/state/{stateId}", 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // PUT
    @Test
    void update_ok() throws Exception {
        MunicipalityRequest rq = req("Municipio Editado");
        MunicipalityResponse updated = resp(20, "Municipio Editado", 7);
        when(service.update(eq(20), eq(7), any(MunicipalityRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}/state/{stateId}", 20, 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_municipality").value(20))
                .andExpect(jsonPath("$.name").value("Municipio Editado"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), eq(7), any(MunicipalityRequest.class)))
                .thenThrow(new EntityNotFoundException("Municipality not found"));

        mvc.perform(put(BASE + "/{id}/state/{stateId}", 999, 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT update con body inválido → 400 por @Valid")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{id}/state/{stateId}", 20, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // DELETE
    /* 
    @Test
    void delete_ok() throws Exception {
        doNothing().when(service).delete(33);

        mvc.perform(delete(BASE + "/{id}", 33))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Municipality not found")).when(service).delete(4040);

        mvc.perform(delete(BASE + "/{id}", 4040))
                .andExpect(status().isNotFound());
    }*/

        // ===========================================
    // GET /api/municipalities/search/{name}
    // ===========================================
    @Test
    @DisplayName("GET search con resultados → 200 y lista")
    void searchByName_ok() throws Exception {
        when(service.findByNameLike("hua"))
                .thenReturn(List.of(resp(1, "Huauchinango", 7), resp(2, "Huejotzingo", 7)));

        mvc.perform(get(BASE + "/search/{name}", "hua"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("hua")));
    }

    @Test
    @DisplayName("GET search sin resultados → 200 y []")
    void searchByName_empty() throws Exception {
        when(service.findByNameLike("zzz")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/search/{name}", "zzz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===========================================
    // GET /api/municipalities/state/{stateId}
    // ===========================================
    @Test
    @DisplayName("GET por ID de estado → 200 con lista")
    void findByStateId_ok() throws Exception {
        when(service.findByStateId(7)).thenReturn(List.of(resp(3, "Teziutlán", 7)));

        mvc.perform(get(BASE + "/state/{stateId}", 7))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].stateId").value(7));
    }

    @Test
    @DisplayName("GET por ID de estado sin resultados → 200 y []")
    void findByStateId_empty() throws Exception {
        when(service.findByStateId(999)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/state/{stateId}", 999))
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
        when(service.findAll()).thenReturn(List.of(resp(1, "Xicotepec", 7)));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }*/

}

