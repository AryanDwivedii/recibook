package com.recipe.likes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long likeId;
    @Column(nullable = false)
    private Long recipeId;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private LocalDateTime timeOfLike;
    @PrePersist
    protected void onCreate(){
        timeOfLike=LocalDateTime.now();
    }
}
