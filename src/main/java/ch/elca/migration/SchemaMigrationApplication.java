package ch.elca.migration;

import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchemaMigrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchemaMigrationApplication.class, args);
    }
}