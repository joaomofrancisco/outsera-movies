package com.github.joaomofrancisco.outsera.movies.infrastructure.commandline;

import com.github.joaomofrancisco.outsera.movies.application.MovieImportService;
import com.github.joaomofrancisco.outsera.movies.infrastructure.csv.CsvDataReader;
import com.opencsv.CSVReader;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.configuration.ConfigUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.stream.Stream;


@QuarkusMain
@RequiredArgsConstructor
public class ArgumentsCommandLine implements QuarkusApplication {

  private final MovieImportService movieImportService;
  private final CsvDataReader csvDataReader;

  @Override
  public int run(String... args) throws Exception {

    if (args.length == 0) {
      Log.error( "*******************************************************************************" );
      Log.error( "É necessario informar o arquivo CSV" );
      Log.error( "*******************************************************************************" );
      if ( !ConfigUtils.getProfiles().contains( "test" ) ) {
        return 1;
      }
    }

    execute(args[0]);

    Quarkus.waitForExit();

    return 0;
  }
  public void execute(String filePath ) {


    try ( CSVReader reader = csvDataReader.createReader( filePath )) {
      Stream< CsvDataReader.MovieDataDto > stream = csvDataReader.stream( reader );
      movieImportService.importMovies( stream );
    } catch ( IOException e ) {
      throw new RuntimeException( e.getMessage(), e );
    }
  }

}

