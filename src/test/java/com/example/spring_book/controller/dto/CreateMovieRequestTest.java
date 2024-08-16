package com.example.spring_book.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateMovieRequestTest {

    @Autowired
    private JacksonTester<CreateMovieRequest> jacksonTester;

    @Test
    void testSerialize() throws IOException {
        CreateMovieRequest createMovieRequest = new CreateMovieRequest("123", "title", 2023, "actors");

        JsonContent<CreateMovieRequest> jsonContent = jacksonTester.write(createMovieRequest);

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.imdbId")
                .extractingJsonPathStringValue("@.imdbId").isEqualTo("123");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.title")
                .extractingJsonPathStringValue("@.title").isEqualTo("title");

        assertThat(jsonContent)
                .hasJsonPathNumberValue("@.year")
                .extractingJsonPathNumberValue("@.year").isEqualTo(2023);

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.actors")
                .extractingJsonPathStringValue("@.actors").isEqualTo("actors");
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\"imdbId\":\"123\",\"title\":\"title\",\"year\":2023,\"actors\":\"actors\"}";

        CreateMovieRequest createMovieRequest = jacksonTester.parseObject(content);

        assertThat(createMovieRequest.imdbId()).isEqualTo("123");
        assertThat(createMovieRequest.title()).isEqualTo("title");
        assertThat(createMovieRequest.year()).isEqualTo(2023);
        assertThat(createMovieRequest.actors()).isEqualTo("actors");
    }
}
