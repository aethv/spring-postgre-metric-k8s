package com.example.spring_movie.service;

import com.example.spring_movie.exception.NotFoundException;
import com.example.spring_movie.model.Movie;
import com.example.spring_movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Import(MovieServiceImpl.class)
class MovieServiceImplTest {

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    void testValidateAndGetMovieByIdWhenExisting() {
        Movie movie = new Movie("123", "title", 2023, "actors");

        given(movieRepository.findById(anyString())).willReturn(Optional.of(movie));

        Movie movieFound = movieService.validateAndGetMovieById(movie.getImdbId());
        assertThat(movieFound).isEqualTo(movie);
    }

    @Test
    void testValidateAndGetMovieByIdWhenNonExisting() {
        given(movieRepository.findById(anyString())).willReturn(Optional.empty());

        Throwable exception = assertThrows(NotFoundException.class,
                () -> movieService.validateAndGetMovieById("123"));
        assertThat(exception.getMessage()).isEqualTo("Movie with id 123 is not found");
    }

}
