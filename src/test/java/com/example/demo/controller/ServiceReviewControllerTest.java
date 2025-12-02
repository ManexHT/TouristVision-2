package com.example.demo.controller;

import com.example.demo.dto.ServiceReviewRequest;
import com.example.demo.dto.ServiceReviewResponse;
import com.example.demo.service.ServiceReviewService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ServiceReviewController.class)
class ServiceReviewControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ServiceReviewService service;

    private static final String BASE = "/api/serviceReviews";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ServiceReviewService serviceReviewService() {
            return mock(ServiceReviewService.class);
        }
    }

    // Helpers
    private ServiceReviewResponse resp(int id, int rating, String comment) {
        return ServiceReviewResponse.builder()
                .id_serviceReview(id)
                .rating(rating)
                .comment(comment)
                .build();
    }

    private ServiceReviewRequest req(int rating, String comment) {
        ServiceReviewRequest r = new ServiceReviewRequest();
        r.setRating(rating);
        r.setComment(comment);
        return r;
    }

    /* 
    // GET all
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, 5, "Excelente")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].rating").value(5));
    }*/

    // GET paginado
    @ParameterizedTest
    @CsvSource({ "0,10", "1,1",
            "2,50",
            "5,5" })
    void pagination_ok(int page, int size) throws Exception {
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(2, 4, "Bueno")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].rating").value(4));
    }

    // GET by ID
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, 3, "Regular"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(3));
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

    // POST
    @Test
    void create_ok() throws Exception {
        ServiceReviewRequest rq = req(5, "Excelente");
        ServiceReviewResponse created = resp(10, 5, "Excelente");
        when(service.create(eq(1), eq(2), Mockito.any(ServiceReviewRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/service/{serviceId}/user/{userId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/serviceReviews/10"))
                .andExpect(jsonPath("$.id_serviceReview").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/service/{serviceId}/user/{userId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // PUT
    @Test
    void update_ok() throws Exception {
        ServiceReviewRequest rq = req(4, "Actualizado");
        ServiceReviewResponse updated = resp(20, 4, "Actualizado");
        when(service.update(eq(20), Mockito.any(ServiceReviewRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Actualizado"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(ServiceReviewRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req(3, "X"))))
                .andExpect(status().isNotFound());
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

    // DELETE
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

    // GET by rating
    @Test
    void findByRating_ok() throws Exception {
        when(service.findByRating(5)).thenReturn(List.of(resp(1, 5, "Excelente")));

        mvc.perform(get(BASE + "/rating/{rating}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findByRating_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Rating not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findByRating_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    // GET by comment keyword
    @Test
    void searchByComment_ok() throws Exception {
        when(service.searchByComment("excel")).thenReturn(List.of(resp(1, 5, "Excelente")));

        mvc.perform(get(BASE + "/search/{keyword}", "excel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment", containsStringIgnoringCase("excel")));
    }

    @Test
    void searchByComment_empty() throws Exception {
        when(service.searchByComment("zzz")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/search/{keyword}", "zzz"))
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
        when(service.findAll()).thenReturn(List.of(resp(1, 5, "Excelente")));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
*/
}
