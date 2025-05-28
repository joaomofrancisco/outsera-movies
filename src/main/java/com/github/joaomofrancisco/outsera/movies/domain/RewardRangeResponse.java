package com.github.joaomofrancisco.outsera.movies.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RewardRangeResponse {

  private List< ProducerRange > min;
  private List< ProducerRange > max;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class ProducerRange {
    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;
  }
}
