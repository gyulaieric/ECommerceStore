package com.gyulaieric.ECommerceStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gyulaieric.ECommerceStore.model.RegistrationDTO;
import com.gyulaieric.ECommerceStore.service.AuthenticationService;

import java.util.Map;

@CrossOrigin()
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public void registerUser(@RequestBody RegistrationDTO body){
        authenticationService.registerUser(body.getUsername(), body.getEmail(), body.getPassword());
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody RegistrationDTO body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
