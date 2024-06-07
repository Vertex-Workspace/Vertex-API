package com.vertex.vertex;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.logging.Logger;

@SpringBootApplication
@EnableKafka
public class VertexApplication {
	public static void main(String[] args) {
		SpringApplication.run(VertexApplication.class, args);
	}

}