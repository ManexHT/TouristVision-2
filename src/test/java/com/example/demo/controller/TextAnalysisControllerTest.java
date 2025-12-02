// package com.example.demo.controller;

// import com.example.demo.dto.TextAnalysisRequest;
// import com.example.demo.dto.TextAnalysisResponse;
// import com.example.demo.service.TextAnalysisService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.persistence.EntityNotFoundException;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(controllers = TextAnalysisController.class)
// class TextAnalysisControllerTest {

//     @Autowired
//     MockMvc mvc;

//     @Autowired
//     ObjectMapper mapper;

//     @Autowired
//     TextAnalysisService service;

//     private static final String BASE = "/api/textAnalysis";

//     @BeforeEach
//     void beforeEach() {
//         reset(service);
//     }

//     @TestConfiguration
//     static class TestConfig {
//         @Bean
//         TextAnalysisService textAnalysisService() {
//             return mock(TextAnalysisService.class);
//         }
//     }

//     // Helpers
//     private TextAnalysisResponse resp(int id, int reviewId, String sentiment, String phrases, String lang) {
//         return TextAnalysisResponse.builder()
//                 .id_textAnalysis(id)
//                 .id_review(reviewId)
//                 .sentiment(sentiment)
//                 .keyPhrases(phrases)
//                 .language(lang)
//                 .build();
//     }

//     private TextAnalysisRequest req(String sentiment, String phrases, String lang) {
//         TextAnalysisRequest r = new TextAnalysisRequest();
//         r.setSentiment(sentiment);
//         r.setKeyPhrases(phrases);
//         r.setLanguage(lang);
//         return r;
//     }

//     // ===========================================
//     // GET /
//     // ===========================================
//     @Test
//     void findAll_ok() throws Exception {
//         when(service.findAll()).thenReturn(List.of(resp(1, 10, "positive", "clean, friendly", "en")));

//         mvc.perform(get(BASE))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].sentiment").value("positive"));
//     }

//     // ===========================================
//     // GET /{id}
//     // ===========================================
//     @Test
//     void findById_ok() throws Exception {
//         when(service.findById(3)).thenReturn(resp(3, 10, "neutral", "average", "es"));

//         mvc.perform(get(BASE + "/{id}", 3))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.language").value("es"));
//     }

//     @Test
//     void findById_notFound() throws Exception {
//         when(service.findById(999)).thenThrow(new EntityNotFoundException("Not found"));

//         mvc.perform(get(BASE + "/{id}", 999))
//                 .andExpect(status().isNotFound());
//     }

//     // ===========================================
//     // GET /review/{reviewId}
//     // ===========================================
//     @Test
//     void findByReviewId_ok() throws Exception {
//         when(service.findByReviewId(10)).thenReturn(resp(1, 10, "negative", "dirty, rude", "en"));

//         mvc.perform(get(BASE + "/review/{reviewId}", 10))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.sentiment").value("negative"));
//     }

//     @Test
//     void findByReviewId_notFound() throws Exception {
//         when(service.findByReviewId(999)).thenThrow(new EntityNotFoundException("Not found"));

//         mvc.perform(get(BASE + "/review/{reviewId}", 999))
//                 .andExpect(status().isNotFound());
//     }

//     // ===========================================
//     // POST /review/{reviewId}
//     // ===========================================
//     @Test
//     void create_ok() throws Exception {
//         TextAnalysisRequest rq = req("positive", "clean, friendly", "en");
//         TextAnalysisResponse created = resp(100, 10, rq.getSentiment(), rq.getKeyPhrases(), rq.getLanguage());
//         when(service.create(eq(10), Mockito.any(TextAnalysisRequest.class))).thenReturn(created);

//         mvc.perform(post(BASE + "/review/{reviewId}", 10)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(rq)))
//                 .andExpect(status().isCreated())
//                 .andExpect(header().string("Location", "/api/textAnalysis/100"))
//                 .andExpect(jsonPath("$.id_textAnalysis").value(100));
//     }

//     @Test
//     void create_invalidBody() throws Exception {
//         mvc.perform(post(BASE + "/review/{reviewId}", 10)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{}"))
//                 .andExpect(status().isBadRequest());
//     }

//     // ===========================================
//     // PUT /{id}
//     // ===========================================
//     /* 
//     @Test
//     void update_ok() throws Exception {
//         TextAnalysisRequest rq = req("neutral", "average", "es");
//         TextAnalysisResponse updated = resp(200, 10, rq.getSentiment(), rq.getKeyPhrases(), rq.getLanguage());
//         when(service.update(eq(200), Mockito.any(TextAnalysisRequest.class))).thenReturn(updated);

//         mvc.perform(put(BASE + "/{id}", 200)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(rq)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.language").value("es"));
//     }

//     @Test
//     void update_notFound() throws Exception {
//         when(service.update(eq(999), Mockito.any(TextAnalysisRequest.class)))
//                 .thenThrow(new EntityNotFoundException("Not found"));

//         mvc.perform(put(BASE + "/{id}", 999)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(mapper.writeValueAsString(req("X", "Y", "Z"))))
//                 .andExpect(status().isNotFound());
//     }
//     */
//     // ===========================================
//     // DELETE /{id}
//     // ===========================================
//     /* 
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
//     */
// }
