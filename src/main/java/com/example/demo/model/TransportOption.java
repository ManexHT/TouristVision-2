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
    @Table(name = "transportOptions")
    public class TransportOption {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_transport")
        @JsonProperty("id_Transport")
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "id_place", referencedColumnName = "id_place")
        @JsonProperty("id_Place")
        private TouristPlace place;

        @Column(name = "type", nullable = false, length = 100)
        @JsonProperty("type")
        private String type;

        @Column(name = "description", columnDefinition = "TEXT")
        @JsonProperty("description")
        private String description;
    }