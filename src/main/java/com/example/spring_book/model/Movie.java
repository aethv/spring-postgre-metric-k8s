package com.example.spring_book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    private String imdbId;

    private String title;
    private Integer year;
    private String actors;
}
