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
@Table(name = "services")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    @JsonProperty("id_Service")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_place", referencedColumnName = "id_place")
    @JsonProperty("id_Place")
    private TouristPlace place;

    @ManyToOne
    @JoinColumn(name = "id_address", referencedColumnName = "id_address")
    @JsonProperty("id_Address")
    private Address address;

    @Column(name = "name", nullable = false, length = 100)
    @JsonProperty("name")
    private String name;

    @Column(name = "type", nullable = false, length = 50)
    @JsonProperty("type")
    private String type;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;

    @Column(name = "priceRange", length = 50)
    @JsonProperty("price range")
    private String priceRange;

    @Column(name = "contactInfo", length = 150)
    @JsonProperty("contact info")
    private String contactInfo;
}
