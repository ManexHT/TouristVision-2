/*package com.example.demo.model;

import java.math.BigDecimal;

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
@Table(name = "imageAnalysis")
public class ImageAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imageAnalysis")
    @JsonProperty("id_ImageAnalysis")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_image", referencedColumnName = "id_image")
    @JsonProperty("id_Image")
    private ReviewImage image;

    @Column(name = "confidence", precision = 4, scale = 3)
    @JsonProperty("confidence")
    private BigDecimal confidence;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;
}*/