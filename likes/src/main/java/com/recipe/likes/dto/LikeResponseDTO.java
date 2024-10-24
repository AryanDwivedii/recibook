package com.recipe.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDTO {
    private Long likeId;
    private Long recipeId;
    private String userId;
    private LocalDateTime timeOfLike;
}
