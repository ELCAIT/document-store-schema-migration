package ch.elca.migration.app.v2;

import ch.elca.migration.train.SchemaVersion;
import ch.elca.migration.train.Train;
import ch.elca.migration.train.TrainService;
import ch.elca.migration.train.TrainService.SchemaVersionCounter;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Train-V2-Controller simulates an application running with schema version V2:
 * - is able to read documents in both schema versions V1 and V2
 * - depending on schema-compatibility-version, writes documents either in schema version V1 or V2
 * </pre>
 */
@RestController
@RequestMapping("/api/train/v2")
@RequiredArgsConstructor
public class TrainV2Controller {

  private final TrainService trainService;

  @GetMapping("train-numbers")
  @Operation(summary = "Returns a list of all train numbers")
  public List<Integer> findTrainNumbers() {
    return trainService.findTrainNumbers();
  }

  @GetMapping
  @Operation(summary = "Returns all trains having a given train number")
  public List<Train> findByTrainNumber(
    @RequestParam(defaultValue = "701") int trainNumber
  ) {
    return trainService.findByNumber(trainNumber);
  }

  @PutMapping
  @Operation(summary = "Inserts or updates a train")
  public Train save(@RequestBody Train train) {
    return trainService.save(train);
  }

  @PutMapping("label")
  @Operation(summary = "Updates the label of all trains having the given train number")
  public List<Train> updateLabel(
    @RequestParam(defaultValue = "701") int trainNumber,
    @RequestParam String newLabel
  ) {
    return trainService.updateLabel(trainNumber, newLabel);
  }

  @GetMapping("count-by-schema-version")
  @Operation(summary = "Returns the number of documents per schema-version")
  List<SchemaVersionCounter> countBySchemaVersion() {
    return trainService.countBySchemaVersion();
  }

  @PutMapping("upgrade")
  @Operation(summary = "Updates the schema of the documents having the given schema-version to the configured schema-compatibility-version")
  public void upgradeDocuments(@RequestParam(defaultValue = "V1") SchemaVersion sourceSchemaVersion) {
    trainService.upgradeDocuments(sourceSchemaVersion);
  }
}
