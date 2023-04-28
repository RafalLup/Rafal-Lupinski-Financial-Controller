package com.example.final_project.api.requests;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserRegistrationRequest(
        @NotNull
        String userName,

        @NotNull(message = "User password cannot be null")
        @NotBlank(message = "User password cannot be blank")
        @Length(min = 12, max = 64, message = "User password cannot be shorter than 12 and longer than 64")
        String password,

        @Email
        String email
) {
}
