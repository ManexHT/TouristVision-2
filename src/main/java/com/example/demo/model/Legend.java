package com.example.demo.model;

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
@Table(name = "legends")
public class Legend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_legend")
    @JsonProperty("id_Legend")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_place", referencedColumnName = "id_place")
    @JsonProperty("id_Place")
    private TouristPlace place;

    @Column(name = "title", nullable = false, length = 150)
    @JsonProperty("title")
    private String title;

    @Column(name = "story", nullable = false, columnDefinition = "TEXT")
    @JsonProperty("story")
    private String story;

    @Column(name = "origin", length = 150)
    @JsonProperty("origin")
    private String origin;
}