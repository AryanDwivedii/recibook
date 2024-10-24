package com.recipe.comments.repo;

import com.recipe.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
    List<Comment> findByUserId(String userId);
    List<Comment> findByRecipeId(Long recipeId);
    void deleteByUserIdAndRecipeId(String userId, Long recipeId);
    long countByRecipeId(Long recipeId);
}
