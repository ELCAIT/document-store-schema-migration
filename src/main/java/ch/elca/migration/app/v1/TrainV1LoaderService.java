package ch.elca.migration.app.v1;

import ch.elca.migration.train.v1.TrainV1;
import ch.elca.migration.train.v1.TrainV1MongoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Loads trains from resource file "train/train-v1.json"
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class TrainV1LoaderService {

  public static final String TRAIN_V1_JSON = "train/train-v1.json";

  private final ObjectMapper objectMapper;
  private final TrainV1MongoRepository trainV1MongoRepository;

  public void loadTrains() {
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(TRAIN_V1_JSON)) {
      TrainV1[] trains = objectMapper.readValue(in, TrainV1[].class);
      Arrays.stream(trains).forEach(trainV1MongoRepository::save);

    } catch (Exception exception) {
      log.error("Exception while reading trains from resource={}", TRAIN_V1_JSON, exception);
    }
  }

  public void clearTrains() {
    trainV1MongoRepository.deleteAll();
    ;
  }

}
