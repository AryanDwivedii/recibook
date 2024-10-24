package com.recipe.comments.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String commentDescription;
    private String userId;
    private Long recipeId;
    private LocalDateTime timeOfComment;
}
