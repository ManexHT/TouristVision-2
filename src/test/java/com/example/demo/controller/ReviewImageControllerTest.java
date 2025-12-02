package com.example.demo.controller;

import com.example.demo.dto.ReviewImageRequest;
import com.example.demo.dto.ReviewImageResponse;
import com.example.demo.service.ReviewImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewImageController.class)
class ReviewImageControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ReviewImageService service;

    private static final String BASE = "/api/reviewImages";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ReviewImageService reviewImageService() {
            return mock(ReviewImageService.class);
        }
    }

    // Helpers
    private ReviewImageResponse resp(int id, int reviewId, String url, String desc) {
        return ReviewImageResponse.builder()
                .id_image(id)
                .id_review(reviewId)
                .url(url)
                .description(desc)
                .build();
    }

    private ReviewImageRequest req(String url, String desc) {
        ReviewImageRequest r = new ReviewImageRequest();
        r.setUrl(url);
        r.setDescription(desc);
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    /* 
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, 10, "url1", "desc1")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("url1"));
    }
                */

    // ===========================================
    // GET /page?page=&size=
    // ===========================================
    @Test
    void findAllWithPagination_ok() throws Exception {
        when(service.findAllWithPagination(1, 5)).thenReturn(List.of(resp(1, 10, "url1", "desc1")));

        mvc.perform(get(BASE + "/page")
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_image").value(1));
    }

    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, 10, "url3", "desc3"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("url3"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // ===========================================
    // GET /review/{reviewId}
    // ===========================================
    @Test
    void findByReviewId_ok() throws Exception {
        when(service.findByReviewId(10)).thenReturn(List.of(resp(1, 10, "url1", "desc1")));

        mvc.perform(get(BASE + "/review/{reviewId}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_review").value(10));
    }

    // ===========================================
    // POST /review/{reviewId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        ReviewImageRequest rq = req("urlX", "descX");
        ReviewImageResponse created = resp(100, 10, rq.getUrl(), rq.getDescription());
        when(service.create(eq(10), Mockito.any(ReviewImageRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/review/{reviewId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/reviewImages/100"))
                .andExpect(jsonPath("$.id_image").value(100));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/review/{reviewId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        ReviewImageRequest rq = req("urlY", "descY");
        ReviewImageResponse updated = resp(200, 10, rq.getUrl(), rq.getDescription());
        when(service.update(eq(200), Mockito.any(ReviewImageRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 200)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("urlY"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(ReviewImageRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("url", "desc"))))
                .andExpect(status().isNotFound());
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
        when(service.findAll()).thenReturn(List.of(resp(1, 10, "url1", "desc1")));

        mvc.perform(get(BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }*/
}
