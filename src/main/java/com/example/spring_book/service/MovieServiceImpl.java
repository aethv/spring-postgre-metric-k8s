package com.example.spring_book.service;

import com.example.spring_book.exception.NotFoundException;
import com.example.spring_book.model.Movie;
import com.example.spring_book.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie validateAndGetMovieById(String imdbId) {
        return movieRepository.findById(imdbId)
                .orElseThrow(() -> new NotFoundException("Movie with id %s is not found".formatted(imdbId)));
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
}
