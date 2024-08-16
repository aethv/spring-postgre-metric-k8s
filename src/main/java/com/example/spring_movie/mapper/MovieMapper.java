package com.example.spring_movie.mapper;

import com.example.spring_movie.controller.dto.CreateMovieRequest;
import com.example.spring_movie.controller.dto.MovieResponse;
import com.example.spring_movie.controller.dto.UpdateMovieRequest;
import com.example.spring_movie.model.Movie;

public interface MovieMapper {

    Movie toMovie(CreateMovieRequest request);

    void updateMovieFromUpdateMovieRequest(Movie movie, UpdateMovieRequest request);

    MovieResponse toMovieResponse(Movie movie);
}
