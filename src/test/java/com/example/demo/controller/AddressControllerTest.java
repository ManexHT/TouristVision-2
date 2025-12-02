package com.example.demo.controller;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.service.AddressService;
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

@WebMvcTest(controllers = AddressController.class)
class AddressControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AddressService service;

    private static final String BASE = "/api/addresses";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        AddressService addressService() {
            return mock(AddressService.class);
        }
    }

    // Helpers
    private AddressResponse resp(int id, String street) {
        return AddressResponse.builder()
                .id_address(id)
                .street(street)
                .neighborhood("Centro")
                .postalCode("73800")
                .latitude(19.82)
                .longitude(-97.36)
                .reference("Frente a la plaza")
                .build();
    }

    private AddressRequest req(String street) {
        AddressRequest r = new AddressRequest();
        r.setStreet(street);
        r.setNeighborhood("Centro");
        r.setPostalCode("73800");
        r.setLatitude(19.82);
        r.setLongitude(-97.36);
        r.setReference("Frente a la plaza");
        return r;
    }

    // ===========================================
    // GET /pagination
    // ===========================================
    @ParameterizedTest
    @CsvSource({"0,10"
                , "1,1",
                    "2,50",
                        "5,5"})
    void pagination_ok(int page, int size) throws Exception {
        when(service.findAllWithPagination(page, size)).thenReturn(List.of(resp(1, "Av. Hidalgo")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].street").value("Av. Hidalgo"));
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
    // GET /{id}
    // ===========================================
    @Test
    void findById_ok() throws Exception {
        when(service.findById(3)).thenReturn(resp(3, "Calle Juárez"));

        mvc.perform(get(BASE + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Calle Juárez"));
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
    // POST /
    // ===========================================
    @Test
    void create_ok() throws Exception {
        AddressRequest rq = req("Calle Reforma");
        AddressResponse created = resp(10, "Calle Reforma");
        when(service.create(Mockito.any(AddressRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/addresses/10"))
                .andExpect(jsonPath("$.id_address").value(10));
    }

    @Test
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ===========================================
    // PUT /{id}
    // ===========================================
    @Test
    void update_ok() throws Exception {
        AddressRequest rq = req("Calle Independencia");
        AddressResponse updated = resp(20, "Calle Independencia");
        when(service.update(eq(20), Mockito.any(AddressRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Calle Independencia"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(999), Mockito.any(AddressRequest.class)))
                .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(put(BASE + "/{id}", 999)
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
    // GET /search/postal/{postalCode}
    // ===========================================
    @Test
    void findByPostalCode_ok() throws Exception {
        when(service.findByPostalCode("73800")).thenReturn(List.of(resp(1, "Av. Hidalgo")));

        mvc.perform(get(BASE + "/search/postal/{postalCode}", "73800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postalCode").value("73800"));
    }

    // ===========================================
    // GET /search/neighborhood/{neighborhood}
    // ===========================================
    @Test
    void findByNeighborhoodLike_ok() throws Exception {
        when(service.findByNeighborhoodLike("centro")).thenReturn(List.of(resp(1, "Av. Hidalgo")));

        mvc.perform(get(BASE + "/search/neighborhood/{neighborhood}", "centro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].neighborhood", containsStringIgnoringCase("centro")));
    }

    @Test
    @DisplayName("GET search sin resultados → 200 y []")
    void findByNeighborhoodLike_empty() throws Exception {
        when(service.findByNeighborhoodLike("fortin")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/search/neighborhood/{neighborhood}", "fortin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===========================================
    // GET /search/street/{street}
    // ===========================================
    @Test
    void findByStreetLike_ok() throws Exception {
        when(service.findByStreetLike("reforma")).thenReturn(List.of(resp(1, "Calle Reforma")));

        mvc.perform(get(BASE + "/search/street/{street}", "reforma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].street", containsStringIgnoringCase("reforma")));
    }

    @Test
    @DisplayName("GET search sin resultados → 200 y []")
    void findByStreetLike_empty() throws Exception {
        when(service.findByStreetLike("benito juarez")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/search/street/{street}", "benito juarez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
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
        when(service.findAllWithPagination(0, 10)).thenReturn(List.of(resp(1, "Av. Hidalgo")));

        mvc.perform(get(BASE + "/pagination")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
