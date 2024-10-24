package com.recipe.likes.repo;

import com.recipe.likes.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LikeRepo extends JpaRepository<Like,Long> {
    List<Like> findByUserId(String userId);
    List<Like> findByRecipeId(Long recipeId);
    void deleteByUserIdAndRecipeId(String userId, Long recipeId);
    long countByRecipeId(Long recipeId);
}

