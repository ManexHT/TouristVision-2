package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceReviewResponse {

    private Integer id_serviceReview;
    private Integer rating;
    private String comment;

    @JsonProperty("service_Id")
    private Integer serviceId; 

    @JsonProperty("user_Id")
    private Integer userId;    
}
