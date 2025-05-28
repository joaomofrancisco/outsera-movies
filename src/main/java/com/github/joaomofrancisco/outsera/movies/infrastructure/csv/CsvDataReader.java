package com.github.joaomofrancisco.outsera.movies.infrastructure.csv;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ApplicationScoped
@RequiredArgsConstructor
public class CsvDataReader {

  private static final char SEPARATOR = ';';


  public CSVReader createReader( String filePath ) throws FileNotFoundException {
    FileReader fileReader = new FileReader( filePath );
    CSVReader csvReader = new CSVReaderBuilder( fileReader )
        .withCSVParser( new CSVParserBuilder().withSeparator( SEPARATOR ).build() )
        .withSkipLines( 1 ) // Pular cabeçalho
        .build();

    return csvReader;
  }

  public Stream< MovieDataDto > stream( CSVReader csvReader ) {
    Spliterator< String[] > spliterator = Spliterators.spliteratorUnknownSize( csvReader.iterator(), Spliterator.ORDERED );

    return StreamSupport.stream( spliterator, false )
        .map( this::readRow );
  }

  private MovieDataDto readRow( String[] row ) {
    if ( row.length >= 5 ) {
      try {
        Integer year = Integer.parseInt( row[ 0 ].trim() );
        String title = row[ 1 ].trim();
        String studios = row[ 2 ].trim();
        String producersString = row[ 3 ].trim();
        boolean winner = "yes".equalsIgnoreCase( row[ 4 ].trim() );

        List< String > producers = parseProducers( producersString );
        return new MovieDataDto()
            .setYear( year )
            .setTitle( title )
            .setStudios( studios )
            .setProducers( producers )
            .setWinner( winner );
      } catch ( Exception e ) {
        throw new RuntimeException( "Erro ao processar linha: " + Arrays.toString( row ) + ". Linha ignorada." + e.getMessage(), e );
      }
    } else {
      throw new RuntimeException( "Linha com formato inesperado (colunas insuficientes): " + Arrays.toString( row ) + ". Linha ignorada." );
    }
  }

  private List< String > parseProducers( String producersString ) {
    if ( producersString == null || producersString.trim().isEmpty() ) {
      return new ArrayList<>();
    }
    String standardizedProducers = producersString.replaceAll( "\s+and\s+", "," );
    return Arrays.stream( standardizedProducers.split( "," ) )
        .map( String::trim )
        .filter( s -> !s.isEmpty() )
        .collect( Collectors.toList() );
  }

  @Setter
  @Getter
  @Accessors(chain = true)
  public static class MovieDataDto {
    private Integer year;
    private String title;
    private String studios;
    private List< String > producers;
    private boolean winner;


  }
}

