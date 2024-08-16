package com.example.spring_movie.controller.dto;

import java.io.Serializable;

public record MovieResponse (
        String imdbId,
        String title,
        Integer year,
        String actors
) implements Serializable {
}
