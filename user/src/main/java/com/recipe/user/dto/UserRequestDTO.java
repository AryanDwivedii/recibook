package com.recipe.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String userName;
    private String userEmail;
    private String userPassword;
}
