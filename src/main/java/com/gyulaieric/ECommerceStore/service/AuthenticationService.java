package com.gyulaieric.ECommerceStore.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public void registerUser(String username, String email, String password){

        if (userRepository.existsByUsername(username)) {
            // return Collections.singletonMap("error", "User already exists");
            throw new IllegalStateException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        userRepository.save(new User(0L, email, username, encodedPassword, authorities));
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
