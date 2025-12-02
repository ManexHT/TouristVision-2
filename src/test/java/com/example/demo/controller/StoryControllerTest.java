package com.example.demo.controller;

import com.example.demo.dto.StoryRequest;
import com.example.demo.dto.StoryResponse;
import com.example.demo.service.StoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StoryController.class)
class StoryControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    StoryService service;

    private static final String BASE = "/api/stories";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        StoryService storyService() {
            return mock(StoryService.class);
        }
    }

    // Helpers
    private StoryResponse resp(int id, int placeId, String history, String traditions) {
        return StoryResponse.builder()
                .id_story(id)
                .id_place(placeId)
                .history(history)
                .traditions(traditions)
                .build();
    }

    private StoryRequest req(String history, String traditions) {
        StoryRequest r = new StoryRequest();
        r.setHistory(history);
        r.setTraditions(traditions);
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    /* 
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, 10, "Origin", "Dance")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].history").value("Origin"));
    }*/

    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, 10, "Legend", "Festival"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.traditions").value("Festival"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
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
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(1, 10, "Story", "Tradition")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].history").value("Story"));
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
    // POST /place/{placeId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        StoryRequest rq = req("Origin", "Dance");
        StoryResponse created = resp(100, 10, rq.getHistory(), rq.getTraditions());
        when(service.create(eq(10), Mockito.any(StoryRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/place/{placeId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/stories/100"))
                .andExpect(jsonPath("$.id_story").value(100));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE + "/place/{placeId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}
    // ===========================================
    /* 
    @Test
    void update_ok() throws Exception {
        StoryRequest rq = req("Updated", "New");
        StoryResponse updated = resp(200, 10, rq.getHistory(), rq.getTraditions());
        when(service.update(eq(200), Mockito.any(StoryRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 200)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.history").value("Updated"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(StoryRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X", "Y"))))
                .andExpect(status().isNotFound());
    }
*/
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
}
