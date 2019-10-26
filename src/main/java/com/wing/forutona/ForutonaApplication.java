package com.wing.forutona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource({"classpath:applicationContext.xml"})
public class ForutonaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForutonaApplication.class, args);
	}

}
