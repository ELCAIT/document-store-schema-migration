# Schema versioning and upgrade in document store
*Implemantation with Java, MongoDB and Spring Data*

## Prerequisites
* In order to start the application, a MongoDB database is required.
* The application contains a docker-compose file which can be used to start a MongoDB database with URI=mongodb://localhost:27017. 
This requires docker-compose to be installed (e.g. with Docker Desktop)
* In order to run the application in IntelliJ, annotation processing needs to be enabled (for Lombok)

## Setup
* Make sure a MongoDB database is available
* Configure the MongoDB URI in application.yml: set property "spring.data.mongodb.uri" (Default: mongodb://localhost:27017)
* Start the Spring Boot application ch.elca.migration.SchemaMigrationApplication, defininig the schema compatibility version using the environment variable SCHEMA_COMPATIBILTY_VERSION:
  * Default / SCHEMA_COMPATIBILTY_VERSION=V1: runs the application that writes documents in schema version V1
  * SCHEMA_COMPATIBILTY_VERSION=V2: runs the application that writes documents in schema version V2 
* If two different instances of the application are started (e.g. with different schema-compatibility-versions), then use configure different ports: set property "server.port" (default: 19991)

## Usage
The UI of the application is a swagger interface available under http://localhost:${server.port} (default: http://localhost:19991)

### Controllers
The UI provides the following controllers:
* Train-V1-Loader-Controller: Data setup in schema-version V1
* Train-V2-Controller: Application with schema-version V2 (new service version)
  * Handling documents with schema-versions V1 and V2
  * Writing in schema-version defined by $SCHEMA_COMPATIBILTY_VERSION.
* Train-V1-Controller: Application with schema-version V1 (previous service version)
  * Handling only documents with schema-version V1
  * Writing documets in schema-version V1

The controllers for V2 and V1 simulate instances of the current (V2) and previous (V1) service versions and to test the interaction between them, especially during the rolling upgrade from V1 to V2.

### Endpoints
The following endpoints are provided by the different controllers
* Train-V1-Loader-Controller
  * POST: /api/train/loader/v1: loads the trains from the JSON file included in the application (train-v1.json) into the MongoDB collection "train"
  * DELETE: /api/train/loader/v1: deletes all trains from the collection "train"
  
* Train-V2-Controller
  * Data operations:
    * GET: /api/train/v2/train-numbers: Returns a list of all train numbers
    * GET: /api/train/v2: Retrieves all trains having a given train number
    * PUT: /api/train/v2: Inserts or updates a train
    * PUT: /api/train/v2/label: Updates the label of all trains having the given train number
  * Schema-version operations:
    * GET /api/train/v2/count-by-schema-version: Returns the number of documents per schema-version
    * PUT /api/train/v2/upgrade: Updates the schema of the documents having the given schema-version to the configured schema-compatibility-version

* Train-V1-Controller
  * Data operations:
    * GET: /api/train/v2/train-numbers: Returns a list of all train numbers
    * GET: /api/train/v2: Retrieves all trains having a given train number
    * PUT: /api/train/v2: Inserts or updates a train
    * PUT: /api/train/v2/label: Updates the label of all trains having the given train number
