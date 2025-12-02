/*package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "textAnalysis")
public class TextAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_textAnalysis")
    @JsonProperty("id_TextAnalysis")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_review", referencedColumnName = "id_review")
    @JsonProperty("id_Review")
    private Review review;

    @Column(name = "sentiment", length = 20)
    @JsonProperty("sentiment")
    private String sentiment;

    @Column(name = "keyPhrases", columnDefinition = "TEXT")
    @JsonProperty("key Phrases")
    private String keyPhrases;

    @Column(name = "language", length = 10)
    @JsonProperty("language")
    private String language;
}
*/