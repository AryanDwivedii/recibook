package com.recipe.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String userEmail;
}
