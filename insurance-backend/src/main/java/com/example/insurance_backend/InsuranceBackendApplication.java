package com.example.insurance_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.example.insurance_backend.mapper")
@SpringBootApplication
public class InsuranceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceBackendApplication.class, args);
	}

}
