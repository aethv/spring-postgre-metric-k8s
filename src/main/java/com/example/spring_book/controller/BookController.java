package com.example.spring_book.controller;

import com.example.spring_book.controller.dto.CreateMovieRequest;
import com.example.spring_book.controller.dto.MovieResponse;
import com.example.spring_book.controller.dto.UpdateMovieRequest;
import com.example.spring_book.mapper.MovieMapper;
import com.example.spring_book.model.Movie;
import com.example.spring_book.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class BookController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping("/")
    public List<MovieResponse> getMovies() {
        return movieService.getMovies()
                .stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }

    @GetMapping("/{imdbId}")
    public MovieResponse getMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovieById(imdbId);
        return movieMapper.toMovieResponse(movie);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieResponse createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest) {
        Movie movie = movieService.saveMovie(movieMapper.toMovie(createMovieRequest));
        return movieMapper.toMovieResponse(movie);
    }

    @PatchMapping("/{imdbId}")
    public MovieResponse updateMovie(@PathVariable String imdbId, @RequestBody UpdateMovieRequest updateMovieRequest) {
        Movie movie = movieService.validateAndGetMovieById(imdbId);
        movieMapper.updateMovieFromUpdateMovieRequest(movie, updateMovieRequest);
        movie = movieService.saveMovie(movie);
        return movieMapper.toMovieResponse(movie);
    }

    @DeleteMapping("/{imdbId}")
    public void deleteMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovieById(imdbId);
        movieService.deleteMovie(movie);
    }
}
