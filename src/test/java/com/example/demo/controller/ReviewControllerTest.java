package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.service.ReviewService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerTest {

        @Autowired
        MockMvc mvc;

        @Autowired
        ObjectMapper mapper;

        @Autowired
        ReviewService service;

        private static final String BASE = "/api/reviews";

        @BeforeEach
        void beforeEach() {
                reset(service);
        }

        @TestConfiguration
        static class TestConfig {
                @Bean
                ReviewService reviewService() {
                        return mock(ReviewService.class);
                }
        }

        // Helpers
        private ReviewResponse resp(int id, int userId, int placeId, String title, String content, int rating) {
                return ReviewResponse.builder()
                                .id_review(id)
                                .id_user(userId)
                                .id_place(placeId)
                                .title(title)
                                .content(content)
                                .rating(rating)
                                .build();
        }

        private ReviewRequest req(String title, String content, int rating) {
                ReviewRequest r = new ReviewRequest();
                r.setTitle(title);
                r.setContent(content);
                r.setRating(rating);
                return r;
        }

        // ===========================================
        // GET /
        // ===========================================
        /*
         * @Test
         * void findAll_ok() throws Exception {
         * when(service.findAll()).thenReturn(List.of(resp(1, 1, 10, "Great",
         * "Loved it", 5)));
         * 
         * mvc.perform(get(BASE))
         * .andExpect(status().isOk())
         * .andExpect(jsonPath("$[0].title").value("Great"));
         * }
         */
        // ===========================================
        // GET /{id}
        // ===========================================
        @Test
        void findById_ok() throws Exception {
                when(service.findById(3)).thenReturn(resp(3, 1, 10, "Nice", "Good place", 4));

                mvc.perform(get(BASE + "/{id}", 3))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Nice"));
        }

        @Test
        void findById_notFound() throws Exception {
                when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

                mvc.perform(get(BASE + "/{id}", 999))
                                .andExpect(status().isNotFound());
        }

        // paginated
        @ParameterizedTest(name = "GET /reviews/pagination?page={0}&pageSize={1} → 200")
        @CsvSource({
                        "0,10",
                        "1,1",
                        "2,50",
                        "5,5"
        })
        void pagination_ok(int page, int size) throws Exception {
                // Simulamos un ReviewResponse de prueba
                when(service.findAllWithPagination(page, size))
                                .thenReturn(List.of(resp(1, 1, 5, "Playa", "Espectacular", 3)));

                mvc.perform(get("/reviews/pagination")
                                .queryParam("page", String.valueOf(page))
                                .queryParam("pageSize", String.valueOf(size)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].userId").value(1))
                                .andExpect(jsonPath("$[0].rating").value(5))
                                .andExpect(jsonPath("$[0].title").value("Playa"))
                                .andExpect(jsonPath("$[0].content").value("Espectacular"))
                                .andExpect(jsonPath("$[0].placeId").value(3));
        }

        @ParameterizedTest(name = "GET /reviews/pagination?page={0}&pageSize={1} inválidos → 400")
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

                mvc.perform(get("/reviews/pagination")
                                .queryParam("page", String.valueOf(page))
                                .queryParam("pageSize", String.valueOf(size)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.error",
                                                containsStringIgnoringCase("Invalid Request Parameters")));
        }

        // ===========================================
        // GET /place/{placeId}?page=&size=
        // ===========================================
        /*
         * @Test
         * void findByPlaceId_ok() throws Exception {
         * when(service.findByPlaceId(10, 1, 10))
         * .thenReturn(List.of(resp(1, 4, 10, "Great", "Loved it", 5)));
         * 
         * mvc.perform(get(BASE + "/place/{placeId}", 10)
         * .param("page", "1")
         * .param("size", "10"))
         * .andExpect(status().isOk())
         * .andExpect(jsonPath("$[0].id_place").value(10));
         * }
         */
        // ===========================================
        // GET /user/{userId}?page=&size=
        // ===========================================
        @Test
        void findByUserId_ok() throws Exception {
                when(service.findByUserId(1, 1, 5)).thenReturn(List.of(resp(1, 1, 10, "Great", "Loved it", 5)));

                mvc.perform(get(BASE + "/user/{userId}", 1)
                                .param("page", "1")
                                .param("size", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id_user").value(1));
        }

        // ===========================================
        // POST /user/{userId}/place/{placeId}
        // ===========================================
        @Test
        void create_ok() throws Exception {
                ReviewRequest rq = req("Amazing", "Perfect stay", 5);
                ReviewResponse created = resp(100, 1, 10, rq.getTitle(), rq.getContent(), rq.getRating());
                when(service.create(eq(1), eq(10), Mockito.any(ReviewRequest.class))).thenReturn(created);

                mvc.perform(post(BASE + "/user/{userId}/place/{placeId}", 1, 10)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(rq)))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/api/reviews/100"))
                                .andExpect(jsonPath("$.id_review").value(100));
        }

        @Test
        void create_invalidBody() throws Exception {
                mvc.perform(post(BASE + "/user/{userId}/place/{placeId}", 1, 10)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest());
        }

        // ===========================================
        // PUT /{id}
        // ===========================================
        @Test
        void update_ok() throws Exception {
                ReviewRequest rq = req("Updated", "Changed content", 4);
                ReviewResponse updated = resp(200, 1, 10, rq.getTitle(), rq.getContent(), rq.getRating());
                when(service.update(eq(200), Mockito.any(ReviewRequest.class))).thenReturn(updated);

                mvc.perform(put(BASE + "/{id}", 200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(rq)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated"));
        }

        @Test
        void update_notFound() throws Exception {
                when(service.update(eq(999), Mockito.any(ReviewRequest.class)))
                                .thenThrow(new EntityNotFoundException("Not found"));

                mvc.perform(put(BASE + "/{id}", 999)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req("X", "Y", 3))))
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
         * @Test
         * 
         * @DisplayName("CORS: Access-Control-Allow-Origin presente")
         * void cors_header_present() throws Exception {
         * when(service.findAll()).thenReturn(Collections.emptyList());
         * 
         * mvc.perform(get(BASE)
         * .header("Origin", "*")
         * .accept(MediaType.APPLICATION_JSON))
         * .andExpect(status().isOk())
         * .andExpect(header().string("Access-Control-Allow-Origin", "*"));
         * }
         * 
         * @Test
         * 
         * @DisplayName("Content negotiation: JSON → application/json")
         * void contentNegotiation_json() throws Exception {
         * when(service.findAll()).thenReturn(List.of(resp(1, 1, 10, "Great",
         * "Loved it", 5)));
         * 
         * mvc.perform(get(BASE)
         * .accept(MediaType.APPLICATION_JSON))
         * .andExpect(status().isOk())
         * .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
         * }
         */
}
