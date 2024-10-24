package com.recipe.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDTO {
    private Long recipeId;
    private String recipeName;
    private String recipeDescription;
    private String userId;
    private LocalDateTime timeOfPosting;
    private String userName;

    public RecipeResponseDTO(Long recipeId, String recipeName, String recipeDescription, LocalDateTime timeOfPosting, String userId, String userName) {
    this.recipeDescription=recipeDescription;
    this.recipeName=recipeName;
    this.recipeId=recipeId;
    this.userId=userId;
    this.timeOfPosting=timeOfPosting;
    this.userName=userName;
    }
}
