package com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository;

import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {

}

