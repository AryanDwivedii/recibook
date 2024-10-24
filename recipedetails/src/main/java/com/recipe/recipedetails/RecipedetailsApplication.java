package com.recipe.recipedetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RecipedetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipedetailsApplication.class, args);
	}

}
