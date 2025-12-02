package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    @JsonProperty("id_Address")
    private Integer id;

    @Column(name = "street")
    @JsonProperty("street")
    private String street;

    @Column(name = "neighborhood")
    @JsonProperty("neighborhood")
    private String neighborhood;

    @Column(name = "postalCode")
    @JsonProperty("postal Code")
    private String postalCode;

    @Column(name = "latitude")
    @JsonProperty("latitude")
    private Double latitude;

    @Column(name = "longitude")
    @JsonProperty("longitude")
    private Double longitude;

    @Column(name = "reference", columnDefinition = "TEXT")
    @JsonProperty("reference")
    private String reference;
}