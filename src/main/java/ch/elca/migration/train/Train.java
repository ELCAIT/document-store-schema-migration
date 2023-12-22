package ch.elca.migration.train;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Train stopping at or passing through a series of train stations (schema-version V2).
 */
@Value
@Builder(toBuilder = true)
@Document
@CompoundIndexes({
  @CompoundIndex(
    name = "v2_number",
    def = "{'schemaVersion': 1, 'number': 1}",
    partialFilter = "{'schemaVersion': 'V2'}"
  )
})
public class Train {
  @NonNull
  String id;

  @NonNull
  SchemaVersion schemaVersion;

  @Version
  Integer optimisticLockingVersion;

  @NonNull
  Integer number;

  String type;

  @NonNull
  String label;

  @NonNull
  String from;

  @NonNull
  String to;

  @NonNull
  List<TrainStation> stations;
}
