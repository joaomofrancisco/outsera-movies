package com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@Accessors(chain = true)
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "movie_year", nullable = false) // "year" pode ser palavra reservada em alguns DBs
  private Integer year;

  @Column(nullable = false)
  private String title;

  @Column
  private String studios;

  @Column
  private boolean winner;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
  @JoinTable(
      name = "movie_producer",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "producer_id")
  )
  private Set< Producer > producers = new HashSet<>();

  @Override
  public boolean equals( Object o ) {
    if ( this == o )
      return true;
    if ( o == null || getClass() != o.getClass() )
      return false;
    Movie movie = ( Movie ) o;
    return winner == movie.winner && Objects.equals( id, movie.id ) && Objects.equals( year, movie.year ) && Objects.equals( title, movie.title ) && Objects.equals( studios, movie.studios ) && Objects.equals( producers, movie.producers );
  }

  @Override
  public int hashCode() {
    return Objects.hash( id, year, title, studios, winner, producers );
  }
}

