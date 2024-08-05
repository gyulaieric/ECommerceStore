package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.User;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void loadUserByUsername() {
        User user = new User();
        user.setUsername("testuser");

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void loadUserByUserName_NonExistentUser_ShouldThrowUsernameNotFoundException() {
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testuser"));
        assertEquals("User is not valid", exception.getMessage());
    }
}
