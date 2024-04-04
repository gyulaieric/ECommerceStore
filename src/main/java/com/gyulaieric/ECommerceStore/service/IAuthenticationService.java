package com.gyulaieric.ECommerceStore.service;

import java.util.Map;

public interface IAuthenticationService {
    void registerUser(String username, String email, String password);
    Map<String, String> loginUser(String username, String password);
}
