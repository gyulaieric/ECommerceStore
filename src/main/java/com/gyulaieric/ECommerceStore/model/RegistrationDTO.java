package com.gyulaieric.ECommerceStore.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class RegistrationDTO {
    @NotEmpty(message = "Username should not be empty or null")
    private String username;
    @NotEmpty(message = "Email should not be empty or null")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password should not be empty or null")
    private String password;

    public RegistrationDTO(){
        super();
    }

    public RegistrationDTO(String username, String email ,String password){
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String toString(){
        return "Registration info: username: " + this.username + " password: " + this.password;
    }
}
