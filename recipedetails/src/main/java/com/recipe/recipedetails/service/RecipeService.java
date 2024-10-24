package com.recipe.recipedetails.service;

import com.recipe.recipedetails.config.WebClientConfig;
import com.recipe.recipedetails.dto.RecipeRequestDTO;
import com.recipe.recipedetails.dto.RecipeResponseDTO;
import com.recipe.recipedetails.dto.UserResponseDTO;
import com.recipe.recipedetails.entity.Recipe;
import com.recipe.recipedetails.repo.RecipeRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepo recipeRepo;
    private final WebClient.Builder webClientBuilder;
    private static final String SERVICE_NAME = "recipe-service";
   // private final ReactiveCircuitBreaker fetchingUserDetails;

    @Value("${user.service.url}")
    private String userServiceHost;

    @CircuitBreaker(name=SERVICE_NAME,fallbackMethod = "defaultMethod")
    public RecipeResponseDTO createRecipe(RecipeRequestDTO recipeRequestDTO){
        Recipe recipe=new Recipe();
        recipe.setRecipeName(recipeRequestDTO.getRecipeName());
        recipe.setRecipeDescription(recipeRequestDTO.getRecipeDescription());
        recipe.setUserId(recipeRequestDTO.getUserId());

        Recipe savedRecipe= recipeRepo.save(recipe);
        UserResponseDTO userResponseDTO=fetchUserDetails(recipeRequestDTO.getUserId());
        return convertToRecipeResponseDTO(savedRecipe,userResponseDTO);
    }

    public List<RecipeResponseDTO> getAllRecipes(){
        List<Recipe> recipes= recipeRepo.findAll();
        return recipes.stream().map(recipe -> {UserResponseDTO userResponseDTO = fetchUserDetails(recipe.getUserId());
        return convertToRecipeResponseDTO(recipe, userResponseDTO);
        }).collect(Collectors.toList());
    }

    public List<RecipeResponseDTO> getRecipeByUserId(String userId){
        List<Recipe> recipes = recipeRepo.findByUserId(userId);
        return recipes.stream().map(recipe -> {
            UserResponseDTO userResponseDTO = fetchUserDetails(recipe.getUserId());
            return convertToRecipeResponseDTO(recipe, userResponseDTO);
        }).collect(Collectors.toList());

    }

    public RecipeResponseDTO getRecipeByRecipeId(Long recipeId){
        Recipe recipe=recipeRepo.getReferenceById(recipeId);
        UserResponseDTO userResponseDTO = fetchUserDetails(recipe.getUserId());
        return convertToRecipeResponseDTO(recipe,userResponseDTO);
    }

    private UserResponseDTO fetchUserDetails(String userId) {
        System.out.println("Fetching userdetails for user:" +userId);
        return webClientBuilder.build().get()
                .uri("http://user_service/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }

    public RecipeResponseDTO defaultMethod(RecipeRequestDTO recipeRequestDTO, Exception e) {
        // Log the exception or handle it appropriately
        System.out.println("Exception: " + e.getMessage());

        // Return a default or fallback response
        RecipeResponseDTO fallbackResponse = new RecipeResponseDTO();
        fallbackResponse.setRecipeName(recipeRequestDTO.getRecipeName());
        fallbackResponse.setRecipeDescription("User Service Not responding. Please try again later!");

        return fallbackResponse;
    }

    private RecipeResponseDTO convertToRecipeResponseDTO(Recipe recipe, UserResponseDTO userResponseDTO) {
        return new RecipeResponseDTO(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getRecipeDescription(),
                recipe.getTimeOfPosting(),
                recipe.getUserId(),
                userResponseDTO.getUserName()
        );
    }


}
