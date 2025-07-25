package com.kaws26.inventoryManagementSystem.services;


import com.kaws26.inventoryManagementSystem.dtos.LoginRequest;
import com.kaws26.inventoryManagementSystem.dtos.RegisterRequest;
import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.UserDto;
import com.kaws26.inventoryManagementSystem.models.User;


public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDto userDto);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);

}
