package com.kenchiku_estimator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kenchiku_estimator.repository")
public class KenchikuEstimatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(KenchikuEstimatorApplication.class, args);
	}

}
