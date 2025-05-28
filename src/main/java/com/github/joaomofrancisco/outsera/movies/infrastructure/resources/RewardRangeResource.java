package com.github.joaomofrancisco.outsera.movies.infrastructure.resources;

import com.github.joaomofrancisco.outsera.movies.application.RewardRangeService;
import com.github.joaomofrancisco.outsera.movies.domain.RewardRangeResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("/api/reward-range")
@RequiredArgsConstructor
public class RewardRangeResource {

  private final RewardRangeService rewardRangeService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public RewardRangeResponse getRewardRange() {
    RewardRangeResponse rewardRangeResponse = rewardRangeService.loadRewardRange();
    return rewardRangeResponse;
  }
}
