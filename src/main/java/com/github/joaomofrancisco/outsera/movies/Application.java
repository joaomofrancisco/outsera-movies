package com.github.joaomofrancisco.outsera.movies;

import com.github.joaomofrancisco.outsera.movies.infrastructure.commandline.ArgumentsCommandLine;
import io.quarkus.hibernate.orm.runtime.cdi.QuarkusArcBeanContainer;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

public class Application {

  public static void main( String... args ) {
    Quarkus.run( ArgumentsCommandLine.class, args );
  }
}
