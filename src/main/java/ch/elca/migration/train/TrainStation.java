package ch.elca.migration.train;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Train station at which a train stops or passes through (schema-version V2).
 */
@Value
@Builder
public class TrainStation {

  @NonNull
  String name;

  String arrival;
  String departure;

  @NonNull
  Boolean isStop;

}
