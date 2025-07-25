package com.kaws26.inventoryManagementSystem.dtos;

import com.kaws26.inventoryManagementSystem.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name is required!")
    private String name;

    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    private UserRole role;
}
