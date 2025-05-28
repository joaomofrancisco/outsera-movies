package com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository;

import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.entity.Producer;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class ProducerRepository implements PanacheRepository< Producer > {
  public Optional< Producer > findByName( String name ) {
    return find( "name", name ).firstResultOptional();
  }


  public Stream< RewardRangeDTO > rewardRangeMinMax() {
    var query = """
      with producer_years_winer_intervals as ( 
      	select
      		mp.producer_id,  
      		mp.movie_id,
      		lag(m.movie_year, 1) over (partition     by        producer_id      order by        m.movie_year) as previous_win,  
              m.movie_year  as following_win, 
              m.movie_year - lag(m.movie_year, 1) over (partition     by        producer_id      order by        m.movie_year) as year_interval 
           from
              movie_producer mp  
          join
              movie m      
                  on     mp.movie_id = m.id
          where m.winner = true
      ),
      producer_years_winer_intervals_min_max as (
      	select
      		pywi.*,
      		row_number() over (partition by pywi.producer_id order by pywi.year_interval) rn_asc,
      		row_number() over (partition by pywi.producer_id order by pywi.year_interval desc) rn_desc
      	from producer_years_winer_intervals pywi
      	where pywi.previous_win is not null
      ),
      r_final as (
      	select 'min' type, p.producer_id, p.previous_win, p.following_win, p.year_interval from producer_years_winer_intervals_min_max p
      	where p.rn_asc = 1
      	union all
      	select 'max' type, p.producer_id, p.previous_win, p.following_win, p.year_interval from producer_years_winer_intervals_min_max p
      	where p.rn_desc = 1
      )
      select p.name, f.type, f.previous_win, f.following_win, f.year_interval from r_final f
      join producer p on f.producer_id = p.id
      order by f.type, p.name, f.year_interval
      """;

    return getEntityManager().createNativeQuery( query, RewardRangeDTO.class ).getResultStream();
  }

  @Getter
  public static class RewardRangeDTO {

    public RewardRangeDTO( String name, String type, Integer previousWin, Integer followingWin, Integer yearInterval ) {
      this.name = name;
      this.type = type;
      this.previousWin = previousWin;
      this.followingWin = followingWin;
      this.yearInterval = yearInterval;
    }

    private String name;
    private String type;
    private Integer previousWin;
    private Integer followingWin;
    private Integer yearInterval;
  }

}

