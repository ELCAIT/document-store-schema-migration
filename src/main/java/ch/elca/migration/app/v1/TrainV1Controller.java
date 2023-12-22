package ch.elca.migration.app.v1;

import ch.elca.migration.train.v1.TrainV1;
import java.util.List;
import ch.elca.migration.train.v1.TrainV1MongoRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Train-V1-Controller simulates an application running with schema version V1 (directly accessing the mongodb-collection with version V1)
 */
@RestController
@RequestMapping("/api/train/v1")
@RequiredArgsConstructor
public class TrainV1Controller {

  private final TrainV1MongoRepository trainV1MongoRepository;

  @GetMapping("train-numbers")
  @Operation(summary = "Returns a list of all train numbers")
  public List<Integer> findTrainNumbers() {
    return trainV1MongoRepository.findTrainNumbers();
  }

  @GetMapping
  @Operation(summary = "Returns all trains having a given train number")
  public List<TrainV1> findByTrainNumber(
    @RequestParam(defaultValue = "701") int trainNumber
  ) {
    return trainV1MongoRepository.findByTrainNumber(trainNumber);
  }

  @PutMapping
  @Operation(summary = "Inserts or updates a train")
  public TrainV1 save(@RequestBody TrainV1 trainV1) {
    return trainV1MongoRepository.save(trainV1);
  }

  @PutMapping("label")
  @Operation(summary = "Updates the label of all trains having the given train number")
  public void updateLabel(
    @RequestParam(defaultValue = "701") int trainNumber,
    @RequestParam String newLabel
  ) {
    trainV1MongoRepository.findByTrainNumber(trainNumber).forEach(
      trainV1 -> updateLabel(trainV1, newLabel)
    );
  }

  private void updateLabel(TrainV1 trainV1, String newLabel) {
    TrainV1 updatedTrainV1 = trainV1.toBuilder()
      .label(newLabel)
      .build();
    trainV1MongoRepository.save(updatedTrainV1);
  }
}