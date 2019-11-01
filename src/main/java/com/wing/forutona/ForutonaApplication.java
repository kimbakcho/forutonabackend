package com.wing.forutona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.nio.charset.Charset;


@SpringBootApplication
@ImportResource({"classpath:applicationContext.xml"})
public class ForutonaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForutonaApplication.class, args);
	}

}
