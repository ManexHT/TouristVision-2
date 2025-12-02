package com.example.demo.controller;

import com.example.demo.dto.TouristPlaceRequest;
import com.example.demo.dto.TouristPlaceResponse;
import com.example.demo.service.TouristPlaceService;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TouristPlaceController.class)
class TouristPlaceControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TouristPlaceService service;

    private static final String BASE = "/api/places";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        TouristPlaceService touristPlaceService() {
            return mock(TouristPlaceService.class);
        }
    }

    // Helpers
    private TouristPlaceResponse resp(int id, String name) {
        return TouristPlaceResponse.builder()
                .id_place(id)
                .name(name)
                .description("Lugar turístico")
                .municipalityId(1)
                .addressId(2)
                .build();
    }

    private TouristPlaceRequest req(String name) {
        TouristPlaceRequest r = new TouristPlaceRequest();
        r.setName(name);
        r.setDescription("Descripción del lugar");
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    /* 
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, "Teziutlán")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Teziutlán"));
    }
*/
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
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(1, "Teziutlán")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Teziutlán"));
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
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("Invalid Request Parameters")));
    }

    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, "Zacatlán"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zacatlán"));
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
    // POST /municipality/{municipalityId}/address/{addressId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        TouristPlaceRequest rq = req("Chignahuapan");
        TouristPlaceResponse created = resp(10, "Chignahuapan");
        when(service.create(eq(1), eq(2), Mockito.any(TouristPlaceRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/municipality/{municipalityId}/address/{addressId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/places/10"))
                .andExpect(jsonPath("$.id_place").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/municipality/{municipalityId}/address/{addressId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}/municipality/{municipalityId}/address/{addressId}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        TouristPlaceRequest rq = req("Xicotepec");
        TouristPlaceResponse updated = resp(20, "Xicotepec");
        when(service.update(eq(20), eq(1), eq(2), Mockito.any(TouristPlaceRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}/municipality/{municipalityId}/address/{addressId}", 20, 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Xicotepec"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), eq(1), eq(2), Mockito.any(TouristPlaceRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}/municipality/{municipalityId}/address/{addressId}", 999, 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT update con body inválido → 400 por @Valid")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{id}/municipality/{municipalityId}/address/{addressId}", 10, 2, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
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
    }*/

    // ===========================================
    // GET /search/name/{name}
    // ===========================================
    @Test
    void findByNameLike_ok() throws Exception {
        when(service.findByNameLike("tezi")).thenReturn(List.of(resp(1, "Teziutlán")));

        mvc.perform(get(BASE + "/search/name/{name}", "tezi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("tezi")));
    }

    @Test
    @DisplayName("GET search sin resultados → 200 y []")
    void findByNameLike_empty() throws Exception {
        when(service.findByNameLike("aca")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/search/name/{name}", "aca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===========================================
    // GET /search/municipality/{municipalityId}
    // ===========================================
    @Test
    void findByMunicipalityId_ok() throws Exception {
        when(service.findByMunicipalityId(1)).thenReturn(List.of(resp(1, "Teziutlán")));

        mvc.perform(get(BASE + "/search/municipality/{municipalityId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].municipalityId").value(1));
    }

    // ===========================================
    // Headers CORS y negociación de contenido
    // ===========================================
    /* 
    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin presente")
    void cors_header_present() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mvc.perform(get(BASE)
                .header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }*/
}
