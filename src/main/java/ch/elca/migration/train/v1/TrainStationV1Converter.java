package ch.elca.migration.train.v1;

import ch.elca.migration.train.TrainStation;
import org.springframework.stereotype.Component;

@Component
public class TrainStationV1Converter {

  public TrainStation fromV1(TrainStationV1 trainStationV1) {
    return TrainStation.builder()
      .name(trainStationV1.getName())
      .arrival(trainStationV1.getArrivalTime())
      .departure(trainStationV1.getDepartureTime())
      .isStop(trainStationV1.getIsStop())
      .build();
  }

  public TrainStationV1 toV1(TrainStation trainStation) {
    return TrainStationV1.builder()
      .name(trainStation.getName())
      .arrivalTime(trainStation.getArrival())
      .departureTime(trainStation.getDeparture())
      .isStop(trainStation.getIsStop())
      .build();
  }
}