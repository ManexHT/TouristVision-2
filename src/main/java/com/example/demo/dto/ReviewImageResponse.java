package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewImageResponse {

    private Integer id_image;
    private Integer id_review;
    private String url;
    private String description;
}
