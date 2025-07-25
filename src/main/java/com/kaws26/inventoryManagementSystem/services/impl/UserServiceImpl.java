package com.kaws26.inventoryManagementSystem.services.impl;

import com.kaws26.inventoryManagementSystem.dtos.LoginRequest;
import com.kaws26.inventoryManagementSystem.dtos.RegisterRequest;
import com.kaws26.inventoryManagementSystem.dtos.Response;
import com.kaws26.inventoryManagementSystem.dtos.UserDto;
import com.kaws26.inventoryManagementSystem.enums.UserRole;
import com.kaws26.inventoryManagementSystem.exceptions.InvalidCredentialsException;
import com.kaws26.inventoryManagementSystem.exceptions.NotFoundException;
import com.kaws26.inventoryManagementSystem.models.User;
import com.kaws26.inventoryManagementSystem.repositories.UserRepository;
import com.kaws26.inventoryManagementSystem.security.JwtUtils;
import com.kaws26.inventoryManagementSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {

        UserRole role=UserRole.MANAGER;

        if(registerRequest.getRole()!=null){
            role=registerRequest.getRole();
        }

        User userToSave=User.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User registered Successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user=userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new NotFoundException("User not found. "));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException("Incorrect Password ");
        }

        String token=jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in. ")
                .role(user.getRole())
                .token(token)
                .expirationTime("6 months")
                .build();
    }

    @Override
    public Response getAllUsers() {

        List<User> users=userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        users.forEach(user->user.setTransactions(null));

        List<UserDto> userDTOS = modelMapper.map(users, new TypeToken<List<UserDto>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();

        User user=userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not Found. "));

        user.setTransactions(null);

        return user;
    }

    @Override
    public Response getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow(
                ()->new NotFoundException("User not found"));

        UserDto userDto=modelMapper.map(user,UserDto.class);
        userDto.setTransactions(null);

        return Response.builder()
                .status(200)
                .message("Success")
                .user(userDto)
                .build();
    }

    @Override
    public Response updateUser(Long id, UserDto userDto) {
        User existingUser=userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User Not found."));

        if(userDto.getEmail()!=null) existingUser.setEmail(userDto.getEmail());
        if(userDto.getPhoneNumber()!=null) existingUser.setPhoneNumber(userDto.getPhoneNumber());
        if(userDto.getName()!=null) existingUser.setName(userDto.getName());
        if(userDto.getRole()!=null) existingUser.setRole(userDto.getRole());
        if(userDto.getPassword()!=null && !userDto.getPassword().isEmpty()) existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User updated successfully")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User Not found"));
        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User deleted successfully")
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));

        UserDto userDto=modelMapper.map(user,UserDto.class);

        userDto.getTransactions().forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDto)
                .build();
    }
}
