package com.realestate.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class ForgotPasswordDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Constructors
    public ForgotPasswordDto() {
    }

    public ForgotPasswordDto(String email) {
        this.email = email;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
