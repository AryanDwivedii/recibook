package com.recipe.user.controller;
import com.recipe.user.dto.UserRequestDTO;
import com.recipe.user.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.recipe.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO=userService.createUser(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO,HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id){
        UserResponseDTO user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
