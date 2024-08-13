package com.example.spring_book.service;

import com.example.spring_book.model.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getMovies();

    Movie validateAndGetMovieById(String imdbId);

    Movie saveMovie(Movie movie);

    void deleteMovie(Movie movie);;
}
