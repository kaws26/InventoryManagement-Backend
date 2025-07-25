package com.kaws26.inventoryManagementSystem.security;

import com.kaws26.inventoryManagementSystem.exceptions.NotFoundException;
import com.kaws26.inventoryManagementSystem.models.User;
import com.kaws26.inventoryManagementSystem.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(username).orElseThrow(()->new NotFoundException("User with Email not found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
