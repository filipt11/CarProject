package com.example.CarProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class MyUserUpdateDto {

    @Size(min=3, max=30, message = "username must be between 3 and 30 chars")
    private String username;

    @NotBlank(message = "email is mandatory")
    @Email(message = "email must be valid")
    private String email;

    private String role;

    private Long id;

    private boolean isBanned;

}
