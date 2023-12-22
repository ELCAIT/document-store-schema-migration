package ch.elca.migration.train.v1;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Train station at which a train stops or passes through (schema-version V1).
 */
@Value
@Builder
public class TrainStationV1 {

  @NonNull
  String name;

  String arrivalTime;
  String departureTime;

  @NonNull
  Boolean isStop;

}
