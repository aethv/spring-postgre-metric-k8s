package com.example.spring_book.mapper;

import com.example.spring_book.controller.dto.CreateMovieRequest;
import com.example.spring_book.controller.dto.MovieResponse;
import com.example.spring_book.controller.dto.UpdateMovieRequest;
import com.example.spring_book.model.Movie;

public interface MovieMapper {

    Movie toMovie(CreateMovieRequest request);

    void updateMovieFromUpdateMovieRequest(Movie movie, UpdateMovieRequest request);

    MovieResponse toMovieResponse(Movie movie);
}
