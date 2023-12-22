package ch.elca.migration.train.v1;

import ch.elca.migration.train.SchemaVersion;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Train stopping at or passing through a series of train stations (schema-version V1).
 */
@Value
@Builder(toBuilder = true)
@Document(collection="train")
@CompoundIndexes({
  @CompoundIndex(
    name = "v1_trainNumber",
    def = "{'schemaVersion': 1, 'trainNumber': 1}",
    partialFilter = "{'schemaVersion': 'V1'}"
  )
})
public class TrainV1 {
  @NonNull
  String id;

  @NonNull
  SchemaVersion schemaVersion;

  @Version
  Integer optimisticLockingVersion;

  @NonNull
  Integer trainNumber;

  String trainType;

  @NonNull
  String label;

  @NonNull
  String from;

  @NonNull
  String to;

  @NonNull
  List<TrainStationV1> trainStations;
}