package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {

    private Integer id_review;
    private Integer id_user;
    private Integer id_place;
    private String title;
    private String content;
    private Integer rating;
}
