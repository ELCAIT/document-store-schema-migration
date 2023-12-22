package ch.elca.migration.train.v1;

import ch.elca.migration.train.Train;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * Data access service for documents with schema version V1.
 * Provides business methods (reading and writing operations on trains)
 *
 * Note:
 * - The interface of the service uses objects in schema version V2. The conversion between versions V1 and V2 is done on-the-fly (using {@link TrainV1Converter}).
 * - The service only handles documents having previous schema version V1 and cannot read or write documents having current schema version V2.
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class TrainV1Service {

  private final TrainV1MongoRepository trainV1MongoRepository;
  private final TrainV1Converter trainV1Converter;

  public List<Integer> findTrainNumbers() {
    return trainV1MongoRepository.findTrainNumbers().stream().sorted().toList();
  }

  public List<Train> findByTrainNumber(int trainNumber) {
    return trainV1MongoRepository.findByTrainNumber(trainNumber).stream()
      .map(trainV1Converter::fromV1)
      .toList();
  }

  public Optional<Train> findById(String id) {
    return trainV1MongoRepository.findById(id)
      .map(trainV1Converter::fromV1);
  }

  public Train save(Train train) {
    TrainV1 trainV1 = trainV1Converter.toV1(train);
    TrainV1 updatedTrainV1 = trainV1MongoRepository.save(trainV1);
    return trainV1Converter.fromV1(updatedTrainV1);
  }
}