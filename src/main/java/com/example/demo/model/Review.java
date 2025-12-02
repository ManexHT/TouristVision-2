package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    @JsonProperty("id_Review")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonProperty("id_User")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "id_place", referencedColumnName = "id_place")
    @JsonProperty("id_Place")
    private TouristPlace place;

    @Column(name = "title", nullable = false, length = 150)
    @JsonProperty("title")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @JsonProperty("content")
    private String content;

    @Column(name = "rating")
    @JsonProperty("rating")
    private Integer rating;
}