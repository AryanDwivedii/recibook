package com.recipe.user.service;

import com.recipe.user.dto.UserRequestDTO;
import com.recipe.user.dto.UserResponseDTO;
import com.recipe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.recipe.user.repo.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        User user=new User();
        user.setUserName(userRequestDTO.getUserName());
        user.setUserEmail(userRequestDTO.getUserEmail());
        user.setUserPassword(userRequestDTO.getUserPassword());

        User savedUser=userRepo.save(user);
        return new UserResponseDTO(savedUser.getUserId(), savedUser.getUserName(), savedUser.getUserEmail());

    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepo.findAll().stream()
                                 .map(this::convertUserToResponseDTO)
                                 .collect(Collectors.toList());
    }

    private UserResponseDTO convertUserToResponseDTO(User user) {
        UserResponseDTO userResponseDTO=new UserResponseDTO();
        userResponseDTO.setUserEmail(user.getUserEmail());
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setUserName(user.getUserName());
        return userResponseDTO;
    }

    public UserResponseDTO getUserById(String id){
        return userRepo.findById(id).map(this::convertUserToResponseDTO).orElseThrow(() -> new RuntimeException("User not found with the id:" +id));
    }
}
