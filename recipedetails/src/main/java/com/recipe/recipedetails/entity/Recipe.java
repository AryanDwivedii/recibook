package com.recipe.recipedetails.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="t_recipes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeId;

    @Column(nullable=false)
    private String recipeName;

    @Column(nullable = false)
    private String recipeDescription;

    @Column(nullable = false)
    private LocalDateTime timeOfPosting;

    @Column(nullable = false)
    private String userId;

    @PrePersist
    protected void onCreate(){
        timeOfPosting=LocalDateTime.now();
    }

}
