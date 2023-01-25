package com.newshp.newshp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.newshp.newshp.model")
@SpringBootApplication
public class NewshpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewshpApplication.class, args);
	}

}
