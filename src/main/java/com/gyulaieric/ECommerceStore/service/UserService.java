package com.gyulaieric.ECommerceStore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gyulaieric.ECommerceStore.repository.UserRepository;

import java.util.Collections;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }

    public Map<String, String> getUsernameById(Long id) {
        if (userRepository.existsById(id)) {
            return Collections.singletonMap("username", userRepository.findById(id).get().getUsername());
        } else {
            return Collections.singletonMap("error", "User does not exist");
        }
    }
}
