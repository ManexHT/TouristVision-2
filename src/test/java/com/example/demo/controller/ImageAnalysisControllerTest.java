// package com.example.demo.controller;

// import com.example.demo.dto.ImageAnalysisRequest;
// import com.example.demo.dto.ImageAnalysisResponse;
// import com.example.demo.service.ImageAnalysisService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.persistence.EntityNotFoundException;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.math.BigDecimal;
// import java.util.Collections;
// import java.util.List;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(controllers = ImageAnalysisController.class)
// class ImageAnalysisControllerTest {

//     @Autowired
//     MockMvc mvc;

//     @Autowired
//     ObjectMapper mapper;

//     @Autowired
//     ImageAnalysisService service;

//     private static final String BASE = "/api/imageAnalysis";

//     @BeforeEach
//     void beforeEach() {
//         reset(service);
//     }

//     @TestConfiguration
//     static class TestConfig {
//         @Bean
//         ImageAnalysisService imageAnalysisService() {
//             return mock(ImageAnalysisService.class);
//         }
//     }

//     // Helpers
//     private ImageAnalysisResponse resp(int id, int imageId, BigDecimal confidence, String desc) {
//         return ImageAnalysisResponse.builder()
//                 .id_imageAnalysis(id)
//                 .id_image(imageId)
//                 .confidence(confidence)
//                 .description(desc)
//                 .build();
//     }

//     private ImageAnalysisRequest req(BigDecimal confidence, String desc) {
//         ImageAnalysisRequest r = new ImageAnalysisRequest();
//         r.setConfidence(confidence);
//         r.setDescription(desc);
//         return r;
//     }

//     // ===========================================
//     // GET /
//     // ===========================================
//     @Test
//     void findAll_ok() throws Exception {
//         when(service.findAll()).thenReturn(List.of(resp(1, 10, new BigDecimal("0.987"), "Detected tree")));

//         mvc.perform(get(BASE))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].description").value("Detected tree"));
//     }

//     // ===========================================
//     // GET /{id}
//     // ===========================================
//     @Test
//     void findById_ok() throws Exception {
//         when(service.findById(3)).thenReturn(resp(3, 10, new BigDecimal("0.876"), "Sky detected"));

//         mvc.perform(get(BASE + "/{id}", 3))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.description").value("Sky detected"));
//     }

//     @Test
//     void findById_notFound() throws Exception {
//         when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

//         mvc.perform(get(BASE + "/{id}", 999))
//                 .andExpect(status().isNotFound());
//     }

//     // ===========================================
//     // GET /image/{imageId}
//     // ===========================================
//     @Test
//     void findByImageId_ok() throws Exception {
//         when(service.findByImageId(10)).thenReturn(List.of(resp(1, 10, new BigDecimal("0.999"), "Mountain")));

//         mvc.perform(get(BASE + "/image/{imageId}", 10))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id_image").value(10));
//     }

//     // ===========================================
//     // POST /image/{imageId}
//     // ===========================================
//     @Test
//     void create_ok() throws Exception {
//         ImageAnalysisRequest rq = req(new BigDecimal("0.888"), "River detected");
//         ImageAnalysisResponse created = resp(100, 10, rq.getConfidence(), rq.getDescription());
//         when(service.create(eq(10), Mockito.any(ImageAnalysisRequest.class))).thenReturn(created);

//         mvc.perform(post(BASE + "/image/{imageId}", 10)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(rq)))
//                 .andExpect(status().isCreated())
//                 .andExpect(header().string("Location", "/api/imageAnalysis/100"))
//                 .andExpect(jsonPath("$.id_imageAnalysis").value(100));
//     }

//     @Test
//     void create_invalidBody() throws Exception {
//         mvc.perform(post(BASE + "/image/{imageId}", 10)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{}"))
//                 .andExpect(status().isBadRequest());
//     }

//     // ===========================================
//     // PUT /{id}
//     // ===========================================
//     @Test
//     void update_ok() throws Exception {
//         ImageAnalysisRequest rq = req(new BigDecimal("0.777"), "Updated label");
//         ImageAnalysisResponse updated = resp(200, 10, rq.getConfidence(), rq.getDescription());
//         when(service.update(eq(200), Mockito.any(ImageAnalysisRequest.class))).thenReturn(updated);

//         mvc.perform(put(BASE + "/{id}", 200)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(rq)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.description").value("Updated label"));
//     }

//     @Test
//     void update_notFound() throws Exception {
//         when(service.update(eq(999), Mockito.any(ImageAnalysisRequest.class)))
//                 .thenThrow(new EntityNotFoundException("Not found"));

//         mvc.perform(put(BASE + "/{id}", 999)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(req(new BigDecimal("0.5"), "X"))))
//                 .andExpect(status().isNotFound());
//     }

//     // ===========================================
//     // DELETE /{id}
//     // ===========================================
//     @Test
//     void delete_ok() throws Exception {
//         doNothing().when(service).delete(33);

//         mvc.perform(delete(BASE + "/{id}", 33))
//                 .andExpect(status().isNoContent());
//     }

//     @Test
//     void delete_notFound() throws Exception {
//         doThrow(new EntityNotFoundException("Not found")).when(service).delete(4040);

//         mvc.perform(delete(BASE + "/{id}", 4040))
//                 .andExpect(status().isNotFound());
//     }

//     // ===========================================
//     // Headers CORS y negociación de contenido
//     // ===========================================
//     @Test
//     @DisplayName("CORS: Access-Control-Allow-Origin presente")
//     void cors_header_present() throws Exception {
//         when(service.findAll()).thenReturn(Collections.emptyList());

//         mvc.perform(get(BASE)
//                 .header("Origin", "*")
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(header().string("Access-Control-Allow-Origin", "*"));
//     }

//     @Test
//     @DisplayName("Content negotiation: JSON → application/json")
//     void contentNegotiation_json() throws Exception {
//         when(service.findAll()).thenReturn(List.of(resp(1, 10, new BigDecimal("0.999"), "Mountain")));

//         mvc.perform(get(BASE)
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//     }
// }
