package com.recipe.comments.service;
import com.recipe.comments.dto.CommentRequestDTO;
import com.recipe.comments.dto.CommentResponseDTO;
import com.recipe.comments.dto.RecipeResponseDTO;
import com.recipe.comments.dto.UserResponseDTO;
import com.recipe.comments.entity.Comment;
import com.recipe.comments.repo.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;
    private final WebClient.Builder webClientBuilder;
    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO) throws WebClientResponseException {
        try {
            UserResponseDTO user = fetchUserDetails(commentRequestDTO.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User with ID" + commentRequestDTO.getUserId() + "not found.");
            }
            RecipeResponseDTO recipe = fetchRecipeDetails(commentRequestDTO.getRecipeId());
            if (recipe == null) {
                throw new IllegalArgumentException("Recipe with ID:" + commentRequestDTO.getRecipeId() + "not found.");
            }
            Comment comment = new Comment();
            comment.setCommentDescription(commentRequestDTO.getCommentDescription());
            comment.setUserId(commentRequestDTO.getUserId());
            comment.setRecipeId(commentRequestDTO.getRecipeId());
            Comment savedComment = commentRepo.save(comment);
            return convertToCommentResponseDTO(savedComment);
        }
        catch (WebClientResponseException e) {
            // Handle HTTP response errors, like 404 Not Found
            if (e.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("Failed to add comment: Invalid user or recipe ID. " + e.getMessage());
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("Failed to add comment due to service error: " + e.getMessage());
            } else {
                throw new RuntimeException("Failed to add comment due to error: " + e.getMessage());
            }
        }
        catch(WebClientRequestException e){
            throw new RuntimeException("Failed to add comment due to service unavailability:" + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to add comment due to error:" +e.getMessage());
        }
    }

    public List<CommentResponseDTO> getCommentsOnARecipe(Long recipeId){

        List<Comment> comments= commentRepo.findByRecipeId(recipeId);
        return comments.stream().map(this::convertToCommentResponseDTO).collect(Collectors.toList());
    }

    public long getNumberOfCommentsOnRecipe(Long recipeId) {
        return commentRepo.countByRecipeId(recipeId);
    }

    public List<CommentResponseDTO> getCommentsByAUser(String userId){
        List<Comment> comments= commentRepo.findByUserId(userId);
        return comments.stream().map(this::convertToCommentResponseDTO).collect(Collectors.toList());
    }

    public void removeComment(String userId, Long recipeId){
        commentRepo.deleteByUserIdAndRecipeId(userId,recipeId);
    }

    public CommentResponseDTO convertToCommentResponseDTO(Comment comment){
        return new CommentResponseDTO(
                comment.getCommentId(),comment.getCommentDescription(),comment.getUserId(), comment.getRecipeId(),  comment.getTimeOfComment()
        );
    }

public UserResponseDTO fetchUserDetails(String userId) {
    try {
        return webClientBuilder.build().get()
                .uri("http://user_service/api/users/{userId}", userId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    return Mono.error(new RuntimeException("User not found with ID: " + userId));
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    return Mono.error(new RuntimeException("User service is unavailable"));
                })
                .bodyToMono(UserResponseDTO.class)
                .block();
    } catch (WebClientResponseException e) {
        throw new RuntimeException("Error fetching user details: " + e.getStatusCode() + " " + e.getMessage());
    }
}

    public RecipeResponseDTO fetchRecipeDetails(Long recipeId) {
        try {
            return webClientBuilder.build().get()
                    .uri("http://recipedb/api/recipes/{recipeId}", recipeId)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        return Mono.error(new RuntimeException("Recipe not found with ID: " + recipeId));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        return Mono.error(new RuntimeException("Recipe service is unavailable"));
                    })
                    .bodyToMono(RecipeResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error fetching recipe details: " + e.getStatusCode() + " " + e.getMessage());


}}}

