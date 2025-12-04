package com.example.CarProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@ComponentScan(basePackages = {"com.example.carProject", "com/example/CarProject/controllers", "com/example/CarProject/services", "com/example/CarProject/entities", "com/example/CarProject/repositories", "com/example/CarProject/utils", "validation"})
@EntityScan(basePackages = "com/example/CarProject/entities")
@EnableJpaRepositories(basePackages = "com/example/CarProject/repositories")
@SpringBootApplication
public class CarProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarProjectApplication.class, args);
	}

}
