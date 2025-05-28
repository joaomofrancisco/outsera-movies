package com.github.joaomofrancisco.outsera.movies.application;

import com.github.joaomofrancisco.outsera.movies.infrastructure.csv.CsvDataReader;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Movie;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Producer;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.MovieRepository;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.ProducerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class MovieImportService {

  private final MovieRepository movieRepository;
  private final ProducerRepository producerRepository;

  @Transactional
  public void importMovies( Stream< CsvDataReader.MovieDataDto > movieDataList ) {
    movieDataList.forEach( dto -> {
          Movie movie = new Movie()
              .setYear( dto.getYear() )
              .setTitle( dto.getTitle() )
              .setStudios( dto.getStudios() )
              .setWinner( dto.isWinner() );

          Set< Producer > producers = new HashSet<>();
          for ( String producerName : dto.getProducers() ) {
            Producer producer = producerRepository.findByName( producerName )
                .orElseGet( () -> {
                  return new Producer().setName( producerName );
                } );
            producers.add( producer );
          }
          movie.setProducers( producers );

          movieRepository.persist( movie );
        }
    );
  }
}

