package com.github.joaomofrancisco.outsera.movies.infrastructure.commandline;

import com.github.joaomofrancisco.outsera.movies.core.DatabaseService;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Movie;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Producer;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.MovieRepository;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.ProducerRepository;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class ArgumentsCommandLineTest {

  @Inject
  private ArgumentsCommandLine argumentsCommandLine;
  @Inject
  private DatabaseService databaseService;
  @Inject
  private MovieRepository movieRepository;
  @Inject
  private ProducerRepository producerRepository;

  @Test
  void shouldExecuteImporter() throws Exception {
    databaseService.clearDataBase();
    String filePath = Thread.currentThread().getContextClassLoader().getResource( "csvs/csv_teste_1.csv" ).getPath();
//    String filePath = new ClassPathResource( "csvs/csv_teste_1.csv" ).getFile().getPath();

    argumentsCommandLine.execute( filePath );

    List< Movie > movies = movieRepository.findAll( Sort.by( "title" ) ).stream().toList();

    List< Producer > producers = producerRepository.findAll( Sort.by( "name" ) ).stream().toList();

    MatcherAssert.assertThat( movies, Matchers.hasSize( 7 ) );
    MatcherAssert.assertThat( movies.get( 0 ).getTitle(), Matchers.equalTo( "Can't Stop the Music"));
    MatcherAssert.assertThat( movies.get( 0 ).isWinner(), Matchers.equalTo( true ));
    MatcherAssert.assertThat( movies.get( 0 ).getProducers(), Matchers.hasSize(1));
    MatcherAssert.assertThat( movies.get( 1 ).getTitle(), Matchers.equalTo( "Cruising"));
    MatcherAssert.assertThat( movies.get( 1 ).getProducers(), Matchers.hasSize(3));
    MatcherAssert.assertThat( movies.get( 1 ).isWinner(), Matchers.equalTo( false ));
    MatcherAssert.assertThat( movies.get( 2 ).getTitle(), Matchers.equalTo( "Friday the 13th"));
    MatcherAssert.assertThat( movies.get( 2 ).getProducers(), Matchers.hasSize(1));
    MatcherAssert.assertThat( movies.get( 2 ).isWinner(), Matchers.equalTo( false ));
    MatcherAssert.assertThat( movies.get( 3 ).getTitle(), Matchers.equalTo( "Raise the Titanic"));
    MatcherAssert.assertThat( movies.get( 3 ).getProducers(), Matchers.hasSize(1));
    MatcherAssert.assertThat( movies.get( 3 ).isWinner(), Matchers.equalTo( false ));
    MatcherAssert.assertThat( movies.get( 4 ).getTitle(), Matchers.equalTo( "The Formula"));
    MatcherAssert.assertThat( movies.get( 4 ).getProducers(), Matchers.hasSize(1));
    MatcherAssert.assertThat( movies.get( 4 ).isWinner(), Matchers.equalTo( false ));
    MatcherAssert.assertThat( movies.get( 5 ).getTitle(), Matchers.equalTo( "The Jazz Singer"));
    MatcherAssert.assertThat( movies.get( 5 ).getProducers(), Matchers.hasSize(1));
    MatcherAssert.assertThat( movies.get( 5 ).isWinner(), Matchers.equalTo( false ));
    MatcherAssert.assertThat( movies.get( 6 ).getTitle(), Matchers.equalTo( "The Nude Bomb"));
    MatcherAssert.assertThat( movies.get( 6 ).getProducers(), Matchers.hasSize(2));
    MatcherAssert.assertThat( movies.get( 6 ).isWinner(), Matchers.equalTo( false ));


    movies.forEach( m -> System.out.println(m.getProducers().size()) );

    MatcherAssert.assertThat( producers, Matchers.hasSize( 6 ) );
    MatcherAssert.assertThat( producers.get( 0 ).getName(), Matchers.equalTo( "Allan Carr"));
    MatcherAssert.assertThat( producers.get( 1 ).getName(), Matchers.equalTo( "Jerry Leider"));
    MatcherAssert.assertThat( producers.get( 2 ).getName(), Matchers.equalTo( "Jerry Weintraub"));
    MatcherAssert.assertThat( producers.get( 3 ).getName(), Matchers.equalTo( "Sean S. Cunningham"));
    MatcherAssert.assertThat( producers.get( 4 ).getName(), Matchers.equalTo( "Steve Shagan"));
    MatcherAssert.assertThat( producers.get( 5 ).getName(), Matchers.equalTo( "William Frye"));


  }

}
