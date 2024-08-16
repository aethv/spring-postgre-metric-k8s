package com.example.spring_movie.controller.dto;

public record MovieResponse (
        String imdbId,
        String title,
        Integer year,
        String actors
) {
}
