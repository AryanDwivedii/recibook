package com.recipe.recipedetails.repo;

import com.recipe.recipedetails.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepo extends JpaRepository<Recipe,Long> {
    List<Recipe> findByUserId(String userId);
}
