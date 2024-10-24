package com.recipe.likes.controller;

import com.recipe.likes.dto.LikeRequestDTO;
import com.recipe.likes.dto.LikeResponseDTO;
import com.recipe.likes.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<LikeResponseDTO> addLike(@RequestBody LikeRequestDTO likeRequestDTO){
        LikeResponseDTO response=likeService.addLike(likeRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/list/{recipeId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikesByRecipe(@PathVariable Long  recipeId){
        List<LikeResponseDTO> likes=likeService.getLikesOnARecipe(recipeId);
        return new ResponseEntity<>(
                likes,HttpStatus.OK
        );
    }
    @GetMapping("/{recipeId}")
    public ResponseEntity<Long> getNumberOfLikesOnRecipe(@PathVariable Long  recipeId){
        Long likes=likeService.getNumberOfLikesOnRecipe(recipeId);
        return new ResponseEntity<>(likes,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikesByUser(@PathVariable String  userId){
        List<LikeResponseDTO> likes=likeService.getLikesByAUser(userId);
        return new ResponseEntity<>(
                likes,HttpStatus.OK
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@RequestParam String userId, @RequestParam Long recipeId){
        likeService.removeLike(userId, recipeId);
    }

}
