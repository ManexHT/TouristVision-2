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
@Table(name = "municipalities")
public class Municipality {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_municipality")
    @JsonProperty("id_Municipality")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_state", 
    referencedColumnName = "id_state", nullable = false)
    @JsonProperty("id_State")
    private State state;

    @Column(name = "name", nullable = false, length = 120)
    @JsonProperty("name")
    private String name;
}
