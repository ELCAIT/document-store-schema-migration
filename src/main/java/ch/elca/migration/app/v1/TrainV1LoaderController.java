package ch.elca.migration.app.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Supports clearing and reloading trains from resource file (in schema-version V1)
 */
@RestController
@RequestMapping("/api/train/loader/v1")
@RequiredArgsConstructor
public class TrainV1LoaderController {

  private final TrainV1LoaderService trainV1LoaderService;

  @PostMapping
  @Operation(summary = "Loads documents from file 'train-v1.json' into mongo-db collection 'train'")
  public void loadTrains() {
     trainV1LoaderService.loadTrains();
  }

  @DeleteMapping
  @Operation(summary = "Deletes all documents from into mongo-db collection 'train'")
  public void clearTrains() {
    trainV1LoaderService.clearTrains();
  }
}