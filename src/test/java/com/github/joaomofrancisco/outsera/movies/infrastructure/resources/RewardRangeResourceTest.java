package com.github.joaomofrancisco.outsera.movies.infrastructure.resources;

import com.github.joaomofrancisco.outsera.movies.core.DatabaseService;
import com.github.joaomofrancisco.outsera.movies.domain.RewardRangeResponse;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Movie;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Producer;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class RewardRangeResourceTest  {

  @Inject
  private DatabaseService databaseService;

  @BeforeEach
  public void clearDatabase() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    databaseService.clearDataBase();
  }

  @Test
  void getRewardRange_shouldReturnMinAndMaxIntervals() throws Exception {

    var producerA = databaseService.createProducer( new Producer().setName( "Producer A" ) );
    databaseService.createMovie(
        new Movie().setYear( 2000 ).setTitle( "Movie A1" ).setStudios( "Studio X" ).setWinner( true ),
        producerA
    );
    databaseService.createMovie(
        new Movie().setYear( 2001 ).setTitle( "Movie A2" ).setStudios( "Studio 2" ).setWinner( true ),
        producerA
    );
    databaseService.createMovie(
        new Movie().setYear( 2001 ).setTitle( "Movie A3" ).setStudios( "Studio X" ).setWinner( true ),
        producerA
    );
    databaseService.createMovie(
        new Movie().setYear( 2004 ).setTitle( "Movie A4" ).setStudios( "Studio 2" ).setWinner( true ),
        producerA
    );


    var producerB = databaseService.createProducer( new Producer().setName( "Producer B" ) );
    databaseService.createMovie(
        new Movie().setYear( 2010 ).setTitle( "Movie B1" ).setStudios( "Studio BB" ).setWinner( true ),
        producerB
    );
    databaseService.createMovie(
        new Movie().setYear( 2010 ).setTitle( "Movie B2" ).setStudios( "Studio CC" ).setWinner( true ),
        producerB
    );

    databaseService.createMovie(
        new Movie().setYear( 2020 ).setTitle( "Movie B3" ).setStudios( "Studio BB" ).setWinner( true ),
        producerB
    );
    databaseService.createMovie(
        new Movie().setYear( 2022 ).setTitle( "Movie B4" ).setStudios( "Studio CC" ).setWinner( true ),
        producerB
    );

    var producerC = databaseService.createProducer( new Producer().setName( "Producer C" ) );
    databaseService.createMovie(
        new Movie().setYear( 2005 ).setTitle( "Movie C1" ).setStudios( "Studio X" ).setWinner( true ),
        producerC
    );


    var producerD = databaseService.createProducer( new Producer().setName( "Producer D" ) );
    databaseService.createMovie(
        new Movie().setYear( 2002 ).setTitle( "Movie D1" ).setStudios( "Studio DD" ).setWinner( true ),
        producerD
    );
    databaseService.createMovie(
        new Movie().setYear( 2004 ).setTitle( "Movie D2" ).setStudios( "Studio DDD" ).setWinner( false ),
        producerD
    );
    databaseService.createMovie(
        new Movie().setYear( 2008 ).setTitle( "Movie D3" ).setStudios( "Studio Dd" ).setWinner( true ),
        producerD, producerC
    );

    var producerE = databaseService.createProducer( new Producer().setName( "Producer E" ) );
    databaseService.createMovie(
        new Movie().setYear( 2015 ).setTitle( "Movie E1" ).setStudios( "Studio DD" ).setWinner( false ),
        producerE
    );

    RewardRangeResponse response = given().get( "/api/reward-range" )
        .then()
        .assertThat()
        .statusCode( HttpStatus.SC_OK )
        .extract().as( RewardRangeResponse.class );

    MatcherAssert.assertThat( response.getMin(), hasSize( 4 ) );

    MatcherAssert.assertThat( response.getMin().get( 0 ).getProducer(), is( "Producer A" ) );
    MatcherAssert.assertThat( response.getMin().get( 0 ).getInterval(), is( 0 ) );
    MatcherAssert.assertThat( response.getMin().get( 0 ).getPreviousWin(), is( 2001 ) );
    MatcherAssert.assertThat( response.getMin().get( 0 ).getFollowingWin(), is( 2001 ) );

    MatcherAssert.assertThat( response.getMin().get( 1 ).getProducer(), is( "Producer B" ) );
    MatcherAssert.assertThat( response.getMin().get( 1 ).getInterval(), is( 0 ) );
    MatcherAssert.assertThat( response.getMin().get( 1 ).getPreviousWin(), is( 2010 ) );
    MatcherAssert.assertThat( response.getMin().get( 1 ).getFollowingWin(), is( 2010 ) );

    MatcherAssert.assertThat( response.getMin().get( 2 ).getProducer(), is( "Producer C" ) );
    MatcherAssert.assertThat( response.getMin().get( 2 ).getInterval(), is( 3 ) );
    MatcherAssert.assertThat( response.getMin().get( 2 ).getPreviousWin(), is( 2005 ) );
    MatcherAssert.assertThat( response.getMin().get( 2 ).getFollowingWin(), is( 2008 ) );

    MatcherAssert.assertThat( response.getMin().get( 3 ).getProducer(), is( "Producer D" ) );
    MatcherAssert.assertThat( response.getMin().get( 3 ).getInterval(), is( 6 ) );
    MatcherAssert.assertThat( response.getMin().get( 3 ).getPreviousWin(), is( 2002 ) );
    MatcherAssert.assertThat( response.getMin().get( 3 ).getFollowingWin(), is( 2008 ) );

    MatcherAssert.assertThat( response.getMax(), hasSize( 4 ) );
    MatcherAssert.assertThat( response.getMax().get( 0 ).getProducer(), is( "Producer A" ) );
    MatcherAssert.assertThat( response.getMax().get( 0 ).getInterval(), is( 3 ) );
    MatcherAssert.assertThat( response.getMax().get( 0 ).getPreviousWin(), is( 2001 ) );
    MatcherAssert.assertThat( response.getMax().get( 0 ).getFollowingWin(), is( 2004 ) );

    MatcherAssert.assertThat( response.getMax().get( 1 ).getProducer(), is( "Producer B" ) );
    MatcherAssert.assertThat( response.getMax().get( 1 ).getInterval(), is( 10 ) );
    MatcherAssert.assertThat( response.getMax().get( 1 ).getPreviousWin(), is( 2010 ) );
    MatcherAssert.assertThat( response.getMax().get( 1 ).getFollowingWin(), is( 2020 ) );

    MatcherAssert.assertThat( response.getMax().get( 2 ).getProducer(), is( "Producer C" ) );
    MatcherAssert.assertThat( response.getMax().get( 2 ).getInterval(), is( 3 ) );
    MatcherAssert.assertThat( response.getMax().get( 2 ).getPreviousWin(), is( 2005 ) );
    MatcherAssert.assertThat( response.getMax().get( 2 ).getFollowingWin(), is( 2008 ) );

    MatcherAssert.assertThat( response.getMax().get( 3 ).getProducer(), is( "Producer D" ) );
    MatcherAssert.assertThat( response.getMax().get( 3 ).getInterval(), is( 6 ) );
    MatcherAssert.assertThat( response.getMax().get( 3 ).getPreviousWin(), is( 2002 ) );
    MatcherAssert.assertThat( response.getMax().get( 3 ).getFollowingWin(), is( 2008 ) );

  }

  @Test
  void getRewardRange_whenNoWinners_shouldReturnEmptyLists() throws Exception {

    RewardRangeResponse response = given().get( "/api/reward-range" )
        .then()
        .assertThat()
        .statusCode( HttpStatus.SC_OK )
        .extract().as( RewardRangeResponse.class );

    MatcherAssert.assertThat( response.getMax(), Matchers.hasSize( 0 ) );
    MatcherAssert.assertThat( response.getMin(), Matchers.hasSize( 0 ) );
  }

  @Test
  void getRewardRange_whenOnlyMovieWinner_shouldReturnEmptyLists() throws Exception {
    Long producerId = databaseService.createProducer( new Producer().setName( "Producer OneWin" ) );

    databaseService.createMovie( new Movie().setYear( 2022 ).setTitle( "Single Win Movie" ).setStudios( "Studio Single" ).setWinner( true ), producerId );

    RewardRangeResponse response = given().get( "/api/reward-range" )
        .then()
        .assertThat()
        .statusCode( HttpStatus.SC_OK )
        .extract().as( RewardRangeResponse.class );

    MatcherAssert.assertThat( response.getMax(), hasSize( 0 ) );
    MatcherAssert.assertThat( response.getMin(), hasSize( 0 ) );

  }

}
