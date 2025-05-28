package com.github.joaomofrancisco.outsera.movies.application;

import com.github.joaomofrancisco.outsera.movies.domain.RewardRangeResponse;
import com.github.joaomofrancisco.outsera.movies.infrastructure.persistence.repository.ProducerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional
public class RewardRangeService {

  private final ProducerRepository producerRepository;

  public RewardRangeResponse loadRewardRange() {
    List< RewardRangeResponse.ProducerRange > producerMax = new LinkedList<>();
    List< RewardRangeResponse.ProducerRange > producerMin = new LinkedList<>();

    producerRepository.rewardRangeMinMax().forEach( p -> {

      RewardRangeResponse.ProducerRange producerRange = new RewardRangeResponse.ProducerRange()
          .setProducer( p.getName() )
          .setInterval( p.getYearInterval() )
          .setFollowingWin( p.getFollowingWin() )
          .setPreviousWin( p.getPreviousWin() );

      if ( p.getType().equals( "min" ) ) {
        producerMin.add( producerRange );
      } else if ( p.getType().equals( "max" ) ) {
        producerMax.add( producerRange );
      } else {
        throw new RuntimeException( "Invalid type: " + p.getType() );
      }
    } );

    return new RewardRangeResponse()
        .setMax( producerMax )
        .setMin( producerMin );
  }
}
