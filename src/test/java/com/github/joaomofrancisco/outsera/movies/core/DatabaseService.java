package com.github.joaomofrancisco.outsera.movies.core;

import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Movie;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Producer;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.MovieRepository;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.ProducerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class DatabaseService {

  @Inject
  private MovieRepository movieRepository;

  @Inject
  private ProducerRepository producerRepository;

  public void clearDataBase() {
    movieRepository.deleteAll();
    producerRepository.deleteAll();
  }

  public Long createProducer( Producer producer ) {
    producerRepository.persist( producer );
    return producer.getId();
  }

  public Long createMovie( Movie movie, Long... idProducer ) {
    Set< Producer > producers = Arrays.asList( idProducer )
        .stream()
        .map( id -> producerRepository.findByIdOptional( id ).orElseThrow() )
        .collect( Collectors.toSet() );

    movieRepository.persist( movie
        .setProducers( producers ) );

    return movie.getId();
  }
}
