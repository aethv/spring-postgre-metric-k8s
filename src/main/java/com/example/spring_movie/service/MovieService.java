package com.example.spring_movie.service;

import com.example.spring_movie.model.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getMovies();

    Movie validateAndGetMovieById(String imdbId);

    Movie saveMovie(Movie movie);

    void deleteMovie(Movie movie);;
}
