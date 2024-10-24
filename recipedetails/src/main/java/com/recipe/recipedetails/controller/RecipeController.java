package com.recipe.recipedetails.controller;

import com.recipe.recipedetails.dto.RecipeRequestDTO;
import com.recipe.recipedetails.dto.RecipeResponseDTO;
import com.recipe.recipedetails.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponseDTO> createRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO){
        RecipeResponseDTO recipeResponseDTO=recipeService.createRecipe(recipeRequestDTO);
        return new ResponseEntity<>(recipeResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> getAllRecipes(){
        List<RecipeResponseDTO> recipes=recipeService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<RecipeResponseDTO>> getRecipeByUserId(@PathVariable String userId){
        List<RecipeResponseDTO> recipes=recipeService.getRecipeByUserId(userId);
        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDTO> getRecipeByRecipeId(@PathVariable Long recipeId){
        return new ResponseEntity<>(recipeService.getRecipeByRecipeId(recipeId),HttpStatus.OK);
    }

}
