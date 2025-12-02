package com.example.demo.controller;

import com.example.demo.dto.TransportOptionRequest;
import com.example.demo.dto.TransportOptionResponse;
import com.example.demo.service.TransportOptionService;
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

@WebMvcTest(controllers = TransportOptionController.class)
class TransportOptionControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TransportOptionService service;

    private static final String BASE = "/api/transports";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        TransportOptionService transportOptionService() {
            return mock(TransportOptionService.class);
        }
    }

    // Helpers
    private TransportOptionResponse resp(int id, int placeId, String type, String desc) {
        return TransportOptionResponse.builder()
                .id_transport(id)
                .id_place(placeId)
                .type(type)
                .description(desc)
                .build();
    }

    private TransportOptionRequest req(String type, String desc) {
        TransportOptionRequest r = new TransportOptionRequest();
        r.setType(type);
        r.setDescription(desc);
        return r;
    }

    // ===========================================
    // GET /
    // ===========================================
    /* 
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1, 10, "Bus", "Local route")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Bus"));
    }
*/
    // ===========================================
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, 10, "Taxi", "24/7 service"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("24/7 service"));
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
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(1, 10, "Bike", "Eco-friendly")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("Bike"));
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
    // GET /search?type=
    // ===========================================
    @Test
    void findByType_ok() throws Exception {
        when(service.findByType("bus")).thenReturn(List.of(resp(1, 10, "Bus", "Local route")));

        mvc.perform(get(BASE + "/search")
                .param("type", "bus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Bus"));
    }

    // ===========================================
    // POST /place/{placeId}
    // ===========================================
    @Test
    void create_ok() throws Exception {
        TransportOptionRequest rq = req("Bus", "Local route");
        TransportOptionResponse created = resp(100, 10, rq.getType(), rq.getDescription());
        when(service.create(eq(10), Mockito.any(TransportOptionRequest.class))).thenReturn(created);

        mvc.perform(post(BASE + "/place/{placeId}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/transports/100"))
                .andExpect(jsonPath("$.id_transport").value(100));
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
    @Test
    void update_ok() throws Exception {
        TransportOptionRequest rq = req("Updated", "New description");
        TransportOptionResponse updated = resp(200, 10, rq.getType(), rq.getDescription());
        when(service.update(eq(200), Mockito.any(TransportOptionRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 200)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Updated"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(TransportOptionRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X", "Y"))))
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
    }*/
}

