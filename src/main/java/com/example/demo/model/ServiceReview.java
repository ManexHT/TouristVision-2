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
@Table(name = "serviceReviews")
public class ServiceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serviceReview")
    @JsonProperty("id_ServiceReview")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_service", referencedColumnName = "id_service")
    @JsonProperty("id_Service")
    private Services service;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonProperty("id_User")
    private AppUser user; 

    @Column(name = "rating")
    @JsonProperty("rating")
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    @JsonProperty("comment")
    private String comment;
}
