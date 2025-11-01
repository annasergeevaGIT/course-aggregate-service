package at.aggregate_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan //https://www.baeldung.com/configuration-properties-in-spring-boot
public class AggregateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregateServiceApplication.class, args);
	}

}
