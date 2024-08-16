package com.example.spring_movie.controller.dto;

public record UpdateMovieRequest (
        String title,
        Integer year,
        String actors
) {
}
