package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "touristPlaces")
public class TouristPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_place")
    @JsonProperty("id_Place")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_municipality", referencedColumnName = "id_municipality", nullable = false)
    @JsonProperty("id_Municipality")
    private Municipality municipality;

    @OneToOne
    @JoinColumn(name = "id_address", referencedColumnName = "id_address")
    @JsonProperty("id_Address")
    private Address address;

    @Column(name = "name", nullable = false, length = 150)
    @JsonProperty("name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;
}

