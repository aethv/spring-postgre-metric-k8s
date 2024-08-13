package com.example.spring_book.controller.dto;

public record MovieResponse (
        String imdbId,
        String title,
        Integer year,
        String actors
) {
}
