package com.example.spring_movie.controller;

import com.example.spring_movie.config.CachingConfig;
import com.example.spring_movie.controller.dto.CreateMovieRequest;
import com.example.spring_movie.controller.dto.MovieResponse;
import com.example.spring_movie.controller.dto.UpdateMovieRequest;
import com.example.spring_movie.mapper.MovieMapper;
import com.example.spring_movie.model.Movie;
import com.example.spring_movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping
    public List<MovieResponse> getMovies() {
        return movieService.getMovies()
                .stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }

    @Cacheable(cacheNames = CachingConfig.MOVIES, key = "#imdbId")
    @GetMapping("/{imdbId}")
    public MovieResponse getMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovieById(imdbId);
        return movieMapper.toMovieResponse(movie);
    }

    @CachePut(cacheNames = CachingConfig.MOVIES, key = "#result.imdbId")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieResponse createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest) {
        Movie movie = movieService.saveMovie(movieMapper.toMovie(createMovieRequest));
        return movieMapper.toMovieResponse(movie);
    }

    @CachePut(cacheNames = CachingConfig.MOVIES, key = "#imdbId")
    @PatchMapping("/{imdbId}")
    public MovieResponse updateMovie(@PathVariable String imdbId, @RequestBody UpdateMovieRequest updateMovieRequest) {
        Movie movie = movieService.validateAndGetMovieById(imdbId);
        movieMapper.updateMovieFromUpdateMovieRequest(movie, updateMovieRequest);
        movie = movieService.saveMovie(movie);
        return movieMapper.toMovieResponse(movie);
    }

    @CacheEvict(cacheNames = CachingConfig.MOVIES, key = "#imdbId")
    @DeleteMapping("/{imdbId}")
    public void deleteMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovieById(imdbId);
        movieService.deleteMovie(movie);
    }
}
