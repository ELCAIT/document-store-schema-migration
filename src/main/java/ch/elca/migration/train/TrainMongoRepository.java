package ch.elca.migration.train;

import ch.elca.migration.train.TrainService.SchemaVersionCounter;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
@Repository
public interface TrainMongoRepository extends MongoRepository<Train, String> {

  @Query("{'schemaVersion' : 'V2', 'number': ?0}")
  List<Train> findByNumber(int number);

  @Aggregation(pipeline = {
    "{'$match': { 'schemaVersion':'V2'}}",
    "{'$project': { 'number': 1 }}"
  })
  List<Integer> findNumbers();

  @Aggregation(pipeline = {
    "{'$match': { 'schemaVersion': ?0 }}",
    "{'$project': { '_id': 1 }}"
  })
  List<String> findIdsBySchemaVersion(SchemaVersion schemaVersion);

  @Aggregation(pipeline = {
    "{'$group': { '_id': '$schemaVersion', 'count': { $sum:  1} }}",
    "{'$project': { 'schemaVersion': '$_id', 'count': 1 }}"
  })
  List<SchemaVersionCounter> countBySchemaVersion();
}