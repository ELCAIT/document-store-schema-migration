package ch.elca.migration.train.v1;

import static ch.elca.migration.train.SchemaVersion.V1;
import static ch.elca.migration.train.SchemaVersion.V2;

import ch.elca.migration.train.Train;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * Converts trains between schema-version V1 and V2:
 * - Forward conversion of {@link TrainV1} to {@link Train}
 * - Backward conversion of {@link Train} to {@link TrainV1}
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class TrainV1Converter {

  private final TrainStationV1Converter trainStationV1Converter;

  public Train fromV1(TrainV1 trainV1) {
    return Train.builder()
      .id(trainV1.getId())
      .schemaVersion(V2)
      .optimisticLockingVersion(trainV1.getOptimisticLockingVersion())
      .type(trainV1.getTrainType())
      .number(trainV1.getTrainNumber())
      .label(trainV1.getLabel())
      .from(trainV1.getFrom())
      .to(trainV1.getTo())
      .stations(trainV1.getTrainStations().stream()
        .map(trainStationV1Converter::fromV1)
        .toList()
      )
      .build();
  }

  public TrainV1 toV1(Train train) {
    return TrainV1.builder()
      .id(train.getId())
      .schemaVersion(V1)
      .optimisticLockingVersion(train.getOptimisticLockingVersion())
      .trainType(train.getType())
      .trainNumber(train.getNumber())
      .label(train.getLabel())
      .from(train.getFrom())
      .to(train.getTo())
      .trainStations(train.getStations().stream()
        .map(trainStationV1Converter::toV1)
        .toList()
      )
      .build();
  }
}