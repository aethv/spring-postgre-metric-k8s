package com.example.spring_movie;

import com.example.spring_movie.controller.dto.CreateMovieRequest;
import com.example.spring_movie.controller.dto.MovieResponse;
import com.example.spring_movie.controller.dto.UpdateMovieRequest;
import com.example.spring_movie.model.Movie;
import com.example.spring_movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportTestcontainers(MyContainers.class)
class MovieApiApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MovieRepository movieRepository;

    private static final String API_MOVIES_URL = "/api/movies";
    private static final String API_MOVIES_IMDB_URL = "/api/movies/%s";

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Test
    void testGetMoviesWhenThereIsNone() {
        ResponseEntity<MovieResponse[]> responseEntity = testRestTemplate.getForEntity(API_MOVIES_URL, MovieResponse[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEmpty();
    }

    @Test
    void testGetMoviesWhenThereIsOne() {
        Movie movie = getDefaultMovie();
        movieRepository.save(movie);

        ResponseEntity<MovieResponse[]> responseEntity = testRestTemplate.getForEntity(API_MOVIES_URL, MovieResponse[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).hasSize(1);
        assertThat(responseEntity.getBody()[0].imdbId()).isEqualTo(movie.getImdbId());
        assertThat(responseEntity.getBody()[0].title()).isEqualTo(movie.getTitle());
        assertThat(responseEntity.getBody()[0].year()).isEqualTo(movie.getYear());
        assertThat(responseEntity.getBody()[0].actors()).isEqualTo(movie.getActors());
    }

    @Test
    void testGetMovieWhenNonExistent() {
        String url = API_MOVIES_IMDB_URL.formatted("123");
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(url, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetMovieWhenExistent() {
        Movie movie = getDefaultMovie();
        movieRepository.save(movie);

        String url = API_MOVIES_IMDB_URL.formatted("123");
        ResponseEntity<MovieResponse> responseEntity = testRestTemplate.getForEntity(url, MovieResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().imdbId()).isEqualTo(movie.getImdbId());
        assertThat(responseEntity.getBody().title()).isEqualTo(movie.getTitle());
        assertThat(responseEntity.getBody().year()).isEqualTo(movie.getYear());
        assertThat(responseEntity.getBody().actors()).isEqualTo(movie.getActors());
    }

    @Test
    void testCreateMovie() {
        CreateMovieRequest createMovieRequest = new CreateMovieRequest("123", "title", 2023, "actors");
        ResponseEntity<MovieResponse> responseEntity = testRestTemplate.postForEntity(
                API_MOVIES_URL, createMovieRequest, MovieResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().imdbId()).isEqualTo(createMovieRequest.imdbId());
        assertThat(responseEntity.getBody().title()).isEqualTo(createMovieRequest.title());
        assertThat(responseEntity.getBody().year()).isEqualTo(createMovieRequest.year());
        assertThat(responseEntity.getBody().actors()).isEqualTo(createMovieRequest.actors());

        Optional<Movie> movieOptional = movieRepository.findById(responseEntity.getBody().imdbId());
        assertThat(movieOptional.isPresent()).isTrue();
        movieOptional.ifPresent(movieCreated -> {
            assertThat(movieCreated.getImdbId()).isEqualTo(createMovieRequest.imdbId());
            assertThat(movieCreated.getTitle()).isEqualTo(createMovieRequest.title());
            assertThat(movieCreated.getYear()).isEqualTo(createMovieRequest.year());
            assertThat(movieCreated.getActors()).isEqualTo(createMovieRequest.actors());
        });
    }

    @Test
    void testUpdateMovie() {
        Movie movie = getDefaultMovie();
        movieRepository.save(movie);

        UpdateMovieRequest updateMovieRequest = new UpdateMovieRequest("newTitle", 2024, "newActors");

        HttpEntity<UpdateMovieRequest> requestUpdate = new HttpEntity<>(updateMovieRequest);
        String url = API_MOVIES_IMDB_URL.formatted(movie.getImdbId());
        ResponseEntity<MovieResponse> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.PATCH, requestUpdate, MovieResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().imdbId()).isEqualTo(movie.getImdbId());
        assertThat(responseEntity.getBody().title()).isEqualTo(updateMovieRequest.title());
        assertThat(responseEntity.getBody().year()).isEqualTo(updateMovieRequest.year());
        assertThat(responseEntity.getBody().actors()).isEqualTo(updateMovieRequest.actors());

        Optional<Movie> movieOptional = movieRepository.findById(responseEntity.getBody().imdbId());
        assertThat(movieOptional.isPresent()).isTrue();
        movieOptional.ifPresent(movieUpdated -> {
            assertThat(movieUpdated.getImdbId()).isEqualTo(movie.getImdbId());
            assertThat(movieUpdated.getTitle()).isEqualTo(updateMovieRequest.title());
            assertThat(movieUpdated.getYear()).isEqualTo(updateMovieRequest.year());
            assertThat(movieUpdated.getActors()).isEqualTo(updateMovieRequest.actors());
        });
    }

    @Test
    void testDeleteMovieWhenNonExistent() {
        String imdbId = "123";
        String url = API_MOVIES_IMDB_URL.formatted(imdbId);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.DELETE, null, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteMovieWhenExistent() {
        Movie movie = getDefaultMovie();
        movieRepository.save(movie);

        String url = API_MOVIES_IMDB_URL.formatted(movie.getImdbId());
        ResponseEntity<MovieResponse> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.DELETE, null, MovieResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Optional<Movie> movieOptional = movieRepository.findById(movie.getImdbId());
        assertThat(movieOptional).isNotPresent();
    }

    private Movie getDefaultMovie() {
        return new Movie("123", "title", 2023, "actors");
    }
}
