package com.example.spring_book.mapper;

import com.example.spring_book.controller.dto.CreateMovieRequest;
import com.example.spring_book.controller.dto.MovieResponse;
import com.example.spring_book.controller.dto.UpdateMovieRequest;
import com.example.spring_book.model.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieMapperImpl implements MovieMapper {

    @Override
    public Movie toMovie(CreateMovieRequest createMovieRequest) {
        if (createMovieRequest == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setImdbId(createMovieRequest.imdbId());
        movie.setTitle(createMovieRequest.title());
        movie.setYear(createMovieRequest.year());
        movie.setActors(createMovieRequest.actors());
        return movie;
    }

    @Override
    public void updateMovieFromUpdateMovieRequest(Movie movie, UpdateMovieRequest updateMovieRequest) {
        if (updateMovieRequest == null) {
            return;
        }
        if (updateMovieRequest.title() != null) {
            movie.setTitle(updateMovieRequest.title());
        }
        if (updateMovieRequest.year() != null) {
            movie.setYear(updateMovieRequest.year());
        }
        if (updateMovieRequest.actors() != null) {
            movie.setActors(updateMovieRequest.actors());
        }
    }

    @Override
    public MovieResponse toMovieResponse(Movie movie) {
        if (movie == null) {
            return null;
        }
        return new MovieResponse(movie.getImdbId(), movie.getTitle(), movie.getYear(), movie.getActors());
    }
}
