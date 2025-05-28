package com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "producer")
@Getter
@Setter
@Accessors(chain = true)
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

//    @ManyToMany(mappedBy = "producers")
//    private Set<Movie> movies = new HashSet<>();

    @Override
    public boolean equals( Object o ) {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        Producer producer = ( Producer ) o;
        return Objects.equals( id, producer.id ) && Objects.equals( name, producer.name );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, name );
    }
}

