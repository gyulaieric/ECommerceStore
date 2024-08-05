package com.gyulaieric.ECommerceStore.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginDTO {
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    private String password;

    public LoginDTO(){
        super();
    }

    public LoginDTO(String username, String password){
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
