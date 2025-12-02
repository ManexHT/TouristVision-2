package com.example.demo.controller;

import com.example.demo.dto.ServicesRequest;
import com.example.demo.dto.ServicesResponse;
import com.example.demo.service.ServicesService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ServicesController.class)
class ServicesControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ServicesService service;

    private static final String BASE = "/api/services";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ServicesService servicesService() {
            return mock(ServicesService.class);
        }
    }

    // Helpers
    private ServicesResponse resp(int id, String name, String type) {
        return ServicesResponse.builder()
                .id_service(id)
                .name(name)
                .type(type)
                .build();
    }

    private ServicesRequest req(String name, String type) {
        ServicesRequest r = new ServicesRequest();
        r.setName(name);
        r.setType(type);
        r.setDescription("Descripción");
        r.setPriceRange("Medio");
        r.setContactInfo("contacto@example.com");
        return r;
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
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(1, "Museo", "Cultural")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("Cultural"));
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
        when(service.findById(3)).thenReturn(resp(3, "Restaurante", "Gastronómico"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurante"));
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
    // POST /place/{placeId}/address/{addressId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        ServicesRequest rq = req("Hotel", "Hospedaje");
        ServicesResponse created = resp(10, "Hotel", "Hospedaje");
        when(service.create(eq(1), eq(2), Mockito.any(ServicesRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/place/{placeId}/address/{addressId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/services/10"))
                .andExpect(jsonPath("$.id_service").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/place/{placeId}/address/{addressId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}/place/{placeId}/address/{addressId}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        ServicesRequest rq = req("Hotel Boutique", "Hospedaje");
        ServicesResponse updated = resp(20, "Hotel Boutique", "Hospedaje");
        when(service.update(eq(20), eq(1), eq(2), Mockito.any(ServicesRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}/place/{placeId}/address/{addressId}", 20, 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hotel Boutique"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), eq(1), eq(2), Mockito.any(ServicesRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}/place/{placeId}/address/{addressId}", 999, 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X", "Y"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT update con body inválido → 400 por @Valid")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{id}/place/{placeId}/address/{addressId}", 700, 2, 5)
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
    }
*/
    // ===========================================
    // GET /search/name/{name}
    // ===========================================
    @Test
    void findByNameLike_ok() throws Exception {
        when(service.findByNameLike("hotel")).thenReturn(List.of(resp(1, "Hotel Teziutlán", "Hospedaje")));

        mvc.perform(get(BASE + "/search/name/{name}", "hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("hotel")));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findByNameLike_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Type not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findByNameLike_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // GET /search/type/{type}
    // ===========================================
    @Test
    void findByType_ok() throws Exception {
        when(service.findByType("Hospedaje")).thenReturn(List.of(resp(1, "Hotel", "Hospedaje")));

        mvc.perform(get(BASE + "/search/type/{type}", "Hospedaje"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Hospedaje"));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findByType_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Type not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findByType_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // GET /search/price/{range}
    // ===========================================
    @Test
    void findByPriceRange_ok() throws Exception {
        when(service.findByPriceRange("Medio")).thenReturn(List.of(resp(1, "Hotel", "Hospedaje")));

        mvc.perform(get(BASE + "/search/price/{range}", "Medio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priceRange").doesNotExist()); // no mapeado en helper
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findByPriceRange_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Price Range not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findByPriceRange_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // GET /search/place-name/{placeName}
    // ===========================================
    @Test
    void findByTouristPlaceName_ok() throws Exception {
        when(service.findByTouristPlaceName("tezi")).thenReturn(List.of(resp(1, "Hotel Teziutlán", "Hospedaje")));

        mvc.perform(get(BASE + "/search/place-name/{placeName}", "tezi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("tezi")));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findByTouristPlaceName_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Tourist Name not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findByTouristPlaceName_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // Headers CORS y negociación de contenido
    // ===========================================
    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin presente")
    void cors_header_present() throws Exception {
        when(service.findAllWithPagination(0, 10)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/pagination")
                .header("Origin", "*")
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("page", "0")
                .queryParam("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    @DisplayName("Content negotiation: JSON → application/json")
    void contentNegotiation_json() throws Exception {
        when(service.findAllWithPagination(0, 10)).thenReturn(List.of(resp(1, "Aguascalientes", "Turístico")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}