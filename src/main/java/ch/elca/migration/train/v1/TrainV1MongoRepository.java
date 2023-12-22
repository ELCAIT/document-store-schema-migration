package ch.elca.migration.train.v1;

import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
@Repository
public interface TrainV1MongoRepository extends MongoRepository<TrainV1, String> {

  @Query("{'schemaVersion' : 'V1', 'trainNumber': ?0}")
  List<TrainV1> findByTrainNumber(int trainNumber);

  @Aggregation(pipeline = {
    "{'$match': { 'schemaVersion': 'V1'}}",
    "{'$project': { 'trainNumber': 1 }}"
  })
  List<Integer> findTrainNumbers();

}