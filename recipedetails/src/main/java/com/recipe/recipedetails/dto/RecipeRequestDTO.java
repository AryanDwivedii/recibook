package com.recipe.recipedetails.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDTO {
    private String recipeName;
    private String recipeDescription;
    private String userId;
}
