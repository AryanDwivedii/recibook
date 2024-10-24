package com.recipe.likes.service;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import com.recipe.likes.dto.LikeRequestDTO;
import com.recipe.likes.dto.LikeResponseDTO;
import com.recipe.likes.dto.RecipeResponseDTO;
import com.recipe.likes.dto.UserResponseDTO;
import com.recipe.likes.entity.Like;
import com.recipe.likes.repo.LikeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;
    private final WebClient webClient;

    public LikeResponseDTO addLike(LikeRequestDTO likeRequestDTO) {
        try {
            UserResponseDTO user = fetchUserDetails(likeRequestDTO.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User with ID" + likeRequestDTO.getUserId() + "not found.");
            }
            RecipeResponseDTO recipe = fetchRecipeDetails(likeRequestDTO.getRecipeId());
            if (recipe == null) {
                throw new IllegalArgumentException("Recipe with ID:" + likeRequestDTO.getRecipeId() + "not found.");
            }
            Like like = new Like();
            like.setUserId(likeRequestDTO.getUserId());
            like.setRecipeId(likeRequestDTO.getRecipeId());
            Like savedLike = likeRepo.save(like);

            LikeResponseDTO likeResponseDTO=convertToLikeResponseDTO(savedLike);


            return likeResponseDTO;
        }
        catch (WebClientResponseException e) {
            // Handle HTTP response errors, like 404 Not Found
            if (e.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("Failed to add like: Invalid user or recipe ID. " + e.getMessage());
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("Failed to add like due to service error: " + e.getMessage());
            } else {
                throw new RuntimeException("Failed to add like due to error: " + e.getMessage());
            }
        }
        catch(WebClientRequestException e){
            throw new RuntimeException("Failed to add like due to service unavailability:" + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to add like due to error:" +e.getMessage());
        }
    }

    public List<LikeResponseDTO> getLikesOnARecipe(Long recipeId){

        List<Like> likes= likeRepo.findByRecipeId(recipeId);
        return likes.stream().map(this::convertToLikeResponseDTO).collect(Collectors.toList());
    }

    public long getNumberOfLikesOnRecipe(Long recipeId) {
        return likeRepo.countByRecipeId(recipeId);
    }

    public List<LikeResponseDTO> getLikesByAUser(String userId){
        List<Like> likes= likeRepo.findByUserId(userId);
        return likes.stream().map(this::convertToLikeResponseDTO).collect(Collectors.toList());
    }

    public void removeLike(String userId,Long recipeId){
        likeRepo.deleteByUserIdAndRecipeId(userId,recipeId);
    }

    public LikeResponseDTO convertToLikeResponseDTO(Like like){
        return new LikeResponseDTO(
                like.getLikeId(), like.getRecipeId(), like.getUserId(),like.getTimeOfLike()
        );
    }

    public UserResponseDTO fetchUserDetails(String userId){

        return webClient.get()
                .uri("http://localhost:8080/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }

    public RecipeResponseDTO fetchRecipeDetails(Long recipeId){
        System.out.println("Fetching recipedetails for recipe:" +recipeId);
        return webClient.get()
                .uri("http://localhost:8081/api/recipes/{recipeId}", recipeId)
                .retrieve()
                .bodyToMono(RecipeResponseDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }
}
