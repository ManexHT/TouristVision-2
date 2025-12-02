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
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_story")
    @JsonProperty("id_Story")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_place", referencedColumnName = "id_place")
    @JsonProperty("id_Place")
    private TouristPlace place;

    @Column(name = "history", columnDefinition = "TEXT")
    @JsonProperty("history")
    private String history;

    @Column(name = "traditions", columnDefinition = "TEXT")
    @JsonProperty("traditions")
    private String traditions;
}