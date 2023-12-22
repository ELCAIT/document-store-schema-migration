package ch.elca.migration.train;

import ch.elca.migration.train.v1.TrainV1Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * Data access service for documents in collection "train".
 * Provides business methods (reading and writing operations on trains) as well as operations on schema-version (upgrade schema-versions of documents)
 *
 * Note:
 * - Supports current schema-version V2 and previous schema-version V1.
 * - Documents are written in schema-version defined by {@link SchemaCompatibilityVersionConfiguration#schemaCompatibilityVersion}
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class TrainService {

  private final TrainMongoRepository trainMongoRepository;
  private final TrainV1Service trainV1Service;
  private final SchemaCompatibilityVersionConfiguration schemaCompatibilityVersionConfiguration;

  public List<Integer> findTrainNumbers() {
    List<Integer> trainNumbersV1 = trainV1Service.findTrainNumbers();
    List<Integer> trainNumbersV2 = trainMongoRepository.findNumbers();

    return Stream.concat(trainNumbersV1.stream(), trainNumbersV2.stream())
      .sorted()
      .toList();
  }

  public List<Train> findByNumber(int trainNumber) {
    List<Train> trainsV2 = trainMongoRepository.findByNumber(trainNumber);
    List<Train> trainsV1 = trainV1Service.findByTrainNumber(trainNumber);

    return Stream.concat(trainsV2.stream(), trainsV1.stream()).toList();
  }

  public Train save(Train train) {
    switch (schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion()) {
      case V1:
        return trainV1Service.save(train);

      case V2:
        return trainMongoRepository.save(train);

      default:
        throw new IllegalStateException("Unsupported schemaCompatibilityVersion=%s. Supported versions: [V1, V2]".formatted(schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion()));
    }
  }

  public List<Train> updateLabel(int trainNumber, String newLabel) {
    List<Train> trains = findByNumber(trainNumber);

    List<Train> updatedTrains = trains.stream()
      .map(train -> train.toBuilder().label(newLabel).build())
      .toList();

    switch (schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion()) {
      case V1:
        return updatedTrains.stream()
          .map(trainV1Service::save)
          .toList();

      case V2:
        return updatedTrains.stream()
          .map(trainMongoRepository::save)
          .toList();

      default:
        throw new IllegalStateException("Unsupported schemaCompatibilityVersion=%s. Supported versions: [V1, V2]".formatted(schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion()));
    }
  }

  public void upgradeDocuments(SchemaVersion sourceSchemaVersion) {
    // Read documents from source-schema-version and write them again in schema-compatibility-version
    List<String> ids = trainMongoRepository.findIdsBySchemaVersion(sourceSchemaVersion);
    for (String id : ids) {
      log.info("Upgrading train.id={} from sourceSchemaVersion={} to schemaCompatibilityVersion={}", id, sourceSchemaVersion, schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion());
      findById(id, sourceSchemaVersion)
        .ifPresent(this::upgradeDocument);
    }
  }

  private Optional<Train> findById(String id, SchemaVersion sourceSchemaVersion) {
    switch (sourceSchemaVersion) {
      case V1:
        return trainV1Service.findById(id);

      case V2:
        return trainMongoRepository.findById(id);

      default:
        throw new IllegalStateException("Unsupported sourceSchemaVersion=%s. Supported versions: [V1, V2]".formatted(sourceSchemaVersion));
    }
  }

  private void upgradeDocument(Train train) {
    switch (schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion()) {
      case V1:
        trainV1Service.save(train);
        break;

      case V2:
        trainMongoRepository.save(train);
        break;

      default:
        throw new IllegalStateException("Unsupported schemaCompatibilityVersion=%s. Supported versions: [V1, V2]".formatted(schemaCompatibilityVersionConfiguration.getSchemaCompatibilityVersion()));
    }
  }

  public List<SchemaVersionCounter> countBySchemaVersion() {
    return trainMongoRepository.countBySchemaVersion();
  }

  @Value
  @Builder
  public static class SchemaVersionCounter {
    @NonNull SchemaVersion schemaVersion;
    @NonNull Integer count;
  }
}
