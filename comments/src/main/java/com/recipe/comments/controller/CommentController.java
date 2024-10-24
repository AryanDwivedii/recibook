package com.recipe.comments.controller;

import com.recipe.comments.dto.CommentRequestDTO;
import com.recipe.comments.dto.CommentResponseDTO;
import com.recipe.comments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentRequestDTO commentRequestDTO){
        CommentResponseDTO response=commentService.addComment(commentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsOnARecipe(@PathVariable Long  recipeId){
        List<CommentResponseDTO> comments=commentService.getCommentsOnARecipe(recipeId);
        return new ResponseEntity<>(
                comments,HttpStatus.OK
        );
    }

    @GetMapping("/count/{recipeId}")
    public ResponseEntity<Long> getNumberOfCommentsOnRecipe(@PathVariable Long  recipeId){
        Long comments =commentService.getNumberOfCommentsOnRecipe(recipeId);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByAUser(@PathVariable String  userId){
        List<CommentResponseDTO> comments=commentService.getCommentsByAUser(userId);
        return new ResponseEntity<>(
                comments,HttpStatus.OK
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@RequestParam String userId, @RequestParam Long recipeId){
        commentService.removeComment(userId, recipeId);
    }



}
