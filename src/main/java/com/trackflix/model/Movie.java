package com.trackflix.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private Long id;
    private String title;
    private String genre;
    private String status; // "Assistindo", "Pendente", "Assistido"
    private Integer rating; // 1-5
    private String notes;
    private String imageUrl;
}