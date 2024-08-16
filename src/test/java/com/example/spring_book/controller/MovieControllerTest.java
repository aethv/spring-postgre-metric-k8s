package com.example.spring_book.controller;

import com.example.spring_book.controller.dto.CreateMovieRequest;
import com.example.spring_book.controller.dto.UpdateMovieRequest;
import com.example.spring_book.exception.NotFoundException;
import com.example.spring_book.mapper.MovieMapperImpl;
import com.example.spring_book.model.Movie;
import com.example.spring_book.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
@Import(MovieMapperImpl.class)
class MovieControllerTest {

    private static final String API_MOVIES_URL = "/api/movies";
    private static final String API_MOVIES_ID_URL = "/api/movies/{imdbId}";

    private static final String JSON_$ = "$";

    private static final String JSON_$_IMDB_ID = "$.imdbId";
    private static final String JSON_$_TITLE = "$.title";
    private static final String JSON_$_YEAR = "$.year";
    private static final String JSON_$_ACTORS = "$.actors";

    private static final String JSON_$_0_IMDB_ID = "$[0].imdbId";
    private static final String JSON_$_0_TITLE = "$[0].title";
    private static final String JSON_$_0_YEAR = "$[0].year";
    private static final String JSON_$_0_ACTORS = "$[0].actors";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @Test
    void testGetMoviesWhenThereIsNone() throws Exception {
        given(movieService.getMovies()).willReturn(Collections.emptyList());

        ResultActions resultActions = mockMvc.perform(get(API_MOVIES_URL))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$, hasSize(0)));
    }

    @Test
    void testGetMoviesWhenThereIsOne() throws Exception {
        Movie movie = getDefaultMovie();
        List<Movie> movies = Collections.singletonList(movie);

        given(movieService.getMovies()).willReturn(movies);

        ResultActions resultActions = mockMvc.perform(get(API_MOVIES_URL))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$, hasSize(1)))
                .andExpect(jsonPath(JSON_$_0_IMDB_ID, is(movie.getImdbId())))
                .andExpect(jsonPath(JSON_$_0_TITLE, is(movie.getTitle())))
                .andExpect(jsonPath(JSON_$_0_YEAR, is(movie.getYear())))
                .andExpect(jsonPath(JSON_$_0_ACTORS, is(movie.getActors())));
    }

    @Test
    void testGetMovieByImdbIdWhenNonExistent() throws Exception {
        given(movieService.validateAndGetMovieById(anyString())).willThrow(NotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get(API_MOVIES_ID_URL, "123"))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void testGetMovieByImdbIdWhenExistent() throws Exception {
        Movie movie = getDefaultMovie();
        given(movieService.validateAndGetMovieById(anyString())).willReturn(movie);

        ResultActions resultActions = mockMvc.perform(get(API_MOVIES_ID_URL, movie.getImdbId()))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_IMDB_ID, is(movie.getImdbId())))
                .andExpect(jsonPath(JSON_$_TITLE, is(movie.getTitle())))
                .andExpect(jsonPath(JSON_$_YEAR, is(movie.getYear())))
                .andExpect(jsonPath(JSON_$_ACTORS, is(movie.getActors())));
    }

    @Test
    void testCreateMovie() throws Exception {
        Movie movie = getDefaultMovie();
        given(movieService.saveMovie(any(Movie.class))).willReturn(movie);

        CreateMovieRequest createMovieRequest = getDefaultCreateMovieRequest();
        ResultActions resultActions = mockMvc.perform(post(API_MOVIES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMovieRequest)))
                .andDo(print());

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_IMDB_ID, is(movie.getImdbId())))
                .andExpect(jsonPath(JSON_$_TITLE, is(movie.getTitle())))
                .andExpect(jsonPath(JSON_$_YEAR, is(movie.getYear())))
                .andExpect(jsonPath(JSON_$_ACTORS, is(movie.getActors())));
    }

    @Test
    void testUpdateMovie() throws Exception {
        Movie movie = getDefaultMovie();
        UpdateMovieRequest updateMovieRequest = getDefaultUpdateMovieRequest();

        given(movieService.validateAndGetMovieById(anyString())).willReturn(movie);
        given(movieService.saveMovie(any(Movie.class))).willReturn(movie);

        ResultActions resultActions = mockMvc.perform(patch(API_MOVIES_ID_URL, movie.getImdbId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMovieRequest)))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_IMDB_ID, is(movie.getImdbId())))
                .andExpect(jsonPath(JSON_$_TITLE, is(updateMovieRequest.title())))
                .andExpect(jsonPath(JSON_$_YEAR, is(updateMovieRequest.year())))
                .andExpect(jsonPath(JSON_$_ACTORS, is(updateMovieRequest.actors())));
    }

    @Test
    void testDeleteMovieWhenExistent() throws Exception {
        Movie movie = getDefaultMovie();

        given(movieService.validateAndGetMovieById(anyString())).willReturn(movie);
        willDoNothing().given(movieService).deleteMovie(any(Movie.class));

        ResultActions resultActions = mockMvc.perform(delete(API_MOVIES_ID_URL, movie.getImdbId()))
                .andDo(print());

        resultActions.andExpect(status().isOk());
    }

    @Test
    void testDeleteMovieWhenNonExistent() throws Exception {
        given(movieService.validateAndGetMovieById(anyString())).willThrow(NotFoundException.class);

        ResultActions resultActions = mockMvc.perform(delete(API_MOVIES_ID_URL, "123"))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    private Movie getDefaultMovie() {
        return new Movie("123", "title", 2023, "actors");
    }

    private CreateMovieRequest getDefaultCreateMovieRequest() {
        return new CreateMovieRequest("123", "title", 2023, "actors");
    }

    private UpdateMovieRequest getDefaultUpdateMovieRequest() {
        return new UpdateMovieRequest("newTitle", 2024, "newActors");
    }
}
