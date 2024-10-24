package com.recipe.comments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;
    @Column(nullable = false)
    private String commentDescription;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private Long recipeId;
    @Column(nullable = false)
    private LocalDateTime timeOfComment;

    @PrePersist
    protected void onCreate(){
        timeOfComment=LocalDateTime.now();
    }
}
