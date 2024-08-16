package com.example.spring_movie.mapper;

import com.example.spring_movie.controller.dto.CreateMovieRequest;
import com.example.spring_movie.controller.dto.MovieResponse;
import com.example.spring_movie.controller.dto.UpdateMovieRequest;
import com.example.spring_movie.model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(MovieMapperImpl.class)
class MovieMapperImplTest {

    @Autowired
    private MovieMapper movieMapper;

    @Test
    void testToMovie() {
        var request = new CreateMovieRequest("123", "title", 2024, "actors");
        Movie movie = movieMapper.toMovie(request);

        assertThat(movie.getImdbId()).isEqualTo("123");
        assertThat(movie.getTitle()).isEqualTo("title");
        assertThat(movie.getYear()).isEqualTo(2024);
        assertThat(movie.getActors()).isEqualTo("actors");
    }

    @ParameterizedTest
    @MethodSource("provideUpdateMovieRequests")
    void testUpdateMovieFromUpdateMovieRequest(UpdateMovieRequest updateMovieRequest, Movie expectedMovie) {
        Movie movie = new Movie("123", "title", 2023, "actors");

        movieMapper.updateMovieFromUpdateMovieRequest(movie, updateMovieRequest);

        assertThat(movie.getImdbId()).isEqualTo(expectedMovie.getImdbId());
        assertThat(movie.getTitle()).isEqualTo(expectedMovie.getTitle());
        assertThat(movie.getYear()).isEqualTo(expectedMovie.getYear());
        assertThat(movie.getActors()).isEqualTo(expectedMovie.getActors());
    }

    private static Stream<Arguments> provideUpdateMovieRequests() {
        return Stream.of(
                Arguments.of(new UpdateMovieRequest("newTitle", null, null), new Movie("123", "newTitle", 2023, "actors")),
                Arguments.of(new UpdateMovieRequest(null, 2024, null), new Movie("123", "title", 2024, "actors")),
                Arguments.of(new UpdateMovieRequest(null, null, "newActors"), new Movie("123", "title", 2023, "newActors")),
                Arguments.of(new UpdateMovieRequest("newTitle", 2024, "newActors"), new Movie("123", "newTitle", 2024, "newActors"))
        );
    }

    @Test
    void testToMovieResponse() {
        Movie movie = new Movie("123", "title", 2024, "actors");

        MovieResponse movieResponse = movieMapper.toMovieResponse(movie);

        assertThat(movieResponse.imdbId()).isEqualTo("123");
        assertThat(movieResponse.title()).isEqualTo("title");
        assertThat(movieResponse.year()).isEqualTo(2024);
        assertThat(movieResponse.actors()).isEqualTo("actors");
    }
}
