package com.example.CarProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class MyUserDto {

    @Size(min=3, max=30, message = "username must be between 3 and 30 chars")
    private String username;

    @NotBlank(message = "email is mandatory")
    @Email(message = "email must be valid")
    private String email;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$", message = "password must meet below requirements: ")
    private String password;

    @NotBlank(message = "password confirmation can not be empty")
    private String password2;

    private String role;

}
