package com.gyulaieric.ECommerceStore.service;

import java.util.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gyulaieric.ECommerceStore.model.User;
import com.gyulaieric.ECommerceStore.model.Role;
import com.gyulaieric.ECommerceStore.repository.RoleRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;

@Service
@Transactional
public class AuthenticationService implements IAuthenticationService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public void registerUser(String username, String email, String password){

        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        userRepository.save(new User(1L, email, username, encodedPassword, authorities));
    }

    public Map<String, String> loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            return Map.of("id", userRepository.findByUsername(username).get().getId().toString(), "jwt", tokenService.generateJwt(auth));
        } catch(AuthenticationException e){
            throw new IllegalStateException("Bad credentials");
        }
    }

}
