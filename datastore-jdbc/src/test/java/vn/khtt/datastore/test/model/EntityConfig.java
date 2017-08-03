package vn.khtt.datastore.test.model;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EntityScan("vn.khtt.datastore.test.model")
@EnableAutoConfiguration
public class EntityConfig {
    public EntityConfig() {
    }
}
