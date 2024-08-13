package com.example.spring_book.controller.dto;

public record UpdateMovieRequest (
        String title,
        Integer year,
        String actors
) {
}
