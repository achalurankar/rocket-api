package com.padfoot.rocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class RocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	   }
	
	@Bean
    public Docket todoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(Predicates.not(PathSelectors.regex("/actuator")))
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build();
    }
}
