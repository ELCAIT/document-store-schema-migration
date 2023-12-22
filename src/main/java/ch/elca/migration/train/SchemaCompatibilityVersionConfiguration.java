package ch.elca.migration.train;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * Configuration for schema compatibility version, defining the schema version, in which the documents are written.
 *
 * This allows a rolling upgrade from the previous to the current schema version.
 * - During the rolling upgrade, that is, while still instances only supporting the previous schema version (V1) are running, schemaCompatibilityVersion will be set to V1
 *   This ensures that all documents are still written in schema version V1, and the instances only supporting V1 are able to read all documents
 * - As soon as all instances only supporting V1 are shut down, schemaCompatibilityVersion can be updated to V2. From that point, documents will be written in schema version V2.
 * </pre>
 */
@Configuration
public class SchemaCompatibilityVersionConfiguration {

  @Value("${schemaCompatibilityVersion}")
  private String schemaCompatibilityVersion;

  public SchemaVersion getSchemaCompatibilityVersion() {
    return SchemaVersion.valueOf(schemaCompatibilityVersion);
  }
}